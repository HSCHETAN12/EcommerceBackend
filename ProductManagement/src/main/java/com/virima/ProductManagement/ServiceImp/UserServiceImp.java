package com.virima.ProductManagement.ServiceImp;


import com.virima.ProductManagement.Entity.*;
import com.virima.ProductManagement.Helper.AES;
import com.virima.ProductManagement.Helper.CheckOutMethods;
import com.virima.ProductManagement.Helper.EmailSender;
import com.virima.ProductManagement.ProductException;
import com.virima.ProductManagement.Repository.*;
import com.virima.ProductManagement.Service.UserService;
import com.virima.ProductManagement.dto.CartRequest;
import com.virima.ProductManagement.dto.UserLogins;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;


@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PromoCodeRepository promoCodeRepository;

    Map<String, Object> map = new HashMap<>();

    @Autowired
    CheckOutMethods checkOutMethods;

    @Autowired
    OrderRepository orderRepository;

    /**
     * This method handles the process of saving a new user to the system.
     * <p>
     * 1. It first checks if the provided email, mobile, and username already exist in the database.
     * If any of these fields already exist, it adds an error message to the BindingResult.
     * <p>
     * 2. If there are any validation errors (like duplicate email, mobile, or username),
     * it collects all the error messages and returns a BAD_REQUEST response with the error details.
     * <p>
     * 3. If there are no validation errors, it proceeds to:
     * - Encrypt the user's password for security purposes.
     * - Generate a 5-digit OTP (One-Time Password) for the user and sends it to their email.
     * - Save the new user in the database.
     * - Save any addresses associated with the user to the database.
     * <p>
     * 4. It sets a session attribute indicating that the OTP has been sent successfully.
     * <p>
     * 5. Finally, it returns an OK response with a success message ("User added successfully").
     *
     * @param user          The user object containing the details to be saved.
     * @param bindingResult The BindingResult object to hold validation errors.
     * @param session       The HTTP session to store any session attributes (like OTP sent status).
     * @return ResponseEntity<Object> A response entity containing either error messages (if validation failed)
     * or a success message (if user creation was successful).
     */


    public ResponseEntity<Object> save(User user, BindingResult bindingResult, HttpSession session) {


        if (userRepository.existsByEmail(user.getEmail()))
            bindingResult.rejectValue("email", "error.email", "Email is alreday exist");
        if (userRepository.existsByMobile(user.getMobile()))
            bindingResult.rejectValue("mobile", "error.mobile", "Number is alreday exist");
        if (userRepository.existsByUsername(user.getUsername()))
            bindingResult.rejectValue("username", "error.username", "username is alreday taken");

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Collect all the error messages
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errors.append(error.getDefaultMessage()).append("\n");
            });

            map.put("message", errors);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // If no validation errors, save the product
        else {
            user.setPassword(AES.encrypt(user.getPassword()));
            int otp = new Random().nextInt(10000, 100000);
            user.setOtp(otp);
            System.err.println(otp);
			    emailSender.sendOtp(user.getEmail(),otp , user.getName());
            userRepository.save(user);

            for (Address addressRequest : user.getAddresses()) {
                Address address = new Address();
                address.setStreet(addressRequest.getStreet());
                address.setCity(addressRequest.getCity());
                address.setState(addressRequest.getState());
                address.setCountry(addressRequest.getCountry());
                address.setPostalCode(addressRequest.getPostalCode());
                address.setUser(user); // Associate address with user
                addressRepository.save(address);
            }


            session.setAttribute("pass", "otp-sent Success");
            Map<String, Object> map = new HashMap<>();
            map.put("message", "User added successfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    /**
     * This method verifies the OTP provided by the user and updates their verification status.
     * It checks if the user exists by ID, compares the provided OTP with the stored one, and if valid,
     * marks the user as verified, creates a new wallet for the user, and saves the updates to the database.
     * If the OTP is invalid or the user is not found, it returns an appropriate error message.
     */

    public ResponseEntity<Object> verifys(int otp, int id) {
        User user = userRepository.findById(id).get();
        if (user != null) {
            if (otp == user.getOtp()) {
                user.setVerified(true);
                user.setOtp(0);
                Wallet wallet = new Wallet();
                wallet.setBalance((long) 0.0); // Initial balance
                wallet.setUser(user); // Set the user for wallet
                user.setWallet(wallet);
                walletRepository.save(wallet);
                userRepository.save(user);
                map.put("message", "user get verified");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("message", "Invalied OTP");
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        } else {
            map.put("message", "User Not Found with given id");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> login(UserLogins userLogins, HttpSession session) {
        User user = userRepository.findByUsername(userLogins.getUsername());
        if (user != null && AES.encrypt(userLogins.getPassword()).equals(user.getPassword())) {
            session.setAttribute("user", user);  // Store admin in session
            map.put("message", "Login Successful");
            System.out.print(session.getAttribute("user"));
            return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
        } else {
            map.put("message", "Invalid login credentials");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> logOut(HttpSession session) {
        session.removeAttribute("user");
        session.setAttribute("message", "Logout sucessfully");
        map.put("message", "LogoutSuccessfull");
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

//    @Transactional
//    public ResponseEntity<Object> addCart(int userId, int productId, int quantity) throws ProductException {
//        Product product;
//        try {
//            product = productRepository.findById(productId).get();
//        }catch (Exception e)
//        {
//            throw new ProductException("No product Found");
//        }
//        if (product == null) {
//            map.put("messsage", "Product not found");
//            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//        }
//        if (product.getStock() < quantity) {
//            map.put("message", "Insufficient stock");
//            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//        }
//
//        // 2. Get or create the user's cart
//        Cart cart = cartRepository.findByUserIdAndStatus(userId, "active");
//        if (cart == null) {
//            cart = new Cart(userId);
//            cart = cartRepository.save(cart);
//        }
//
//        // 3. Check if the product is already in the cart
//        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
//        if (cartItem == null) {
//            cartItem = new CartItem(productId, quantity);
//            cartItem.setCart(cart);
//            cartItemRepository.save(cartItem);
//        } else {
//            // If product is already in the cart, update the quantity
//            cartItem.setQuantity(cartItem.getQuantity() + quantity);
//            cartItemRepository.save(cartItem);
//        }
//        product.setStock(product.getStock() - cartItem.getQuantity());
//        productRepository.save(product);
//        // 4. Update the total amount of the cart
//        cart.updateTotalAmount(product);
//        cartRepository.save(cart);
//
//        map.put("message", "Item added to the cart");
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }

    @Transactional
    public ResponseEntity<Object> addProductsToCart(int userId, List<CartRequest> products) throws ProductException {
        // Get or create the user's cart
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "active");
        if (cart == null) {
            cart = new Cart(userId);
            cart = cartRepository.save(cart);
        }

        // Process each product in the request
        for (CartRequest productRequest : products) {
            // Validate if the product exists and check stock availability
            Product product;
            try {
                product = productRepository.findById(productRequest.getProductId()).get();
            } catch (RuntimeException e) {
                throw new ProductException("No product found");
            }
            if (product == null) {
                map.put("messsage", "Product not found");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }
            if (product.getStatus().equals("Not Avaliable")) {
                map.put("message", "Product Not Avaiable");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }
            if (product.getStock() < productRequest.getQuantity()) {
                map.put("message", "Insufficient stock");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }

            // Check if the product is already in the cart
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productRequest.getProductId());
            if (cartItem == null) {
                // If not in the cart, add it
                cartItem = new CartItem(productRequest.getProductId(), productRequest.getQuantity());
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
            } else {
                // If already in the cart, update the quantity
                cartItem.setQuantity(cartItem.getQuantity() + productRequest.getQuantity());
                cartItemRepository.save(cartItem);
            }
            int stock = product.getStock() - cartItem.getQuantity();
            if (stock <= 0) {
                product.setStatus("Not Avaliable");
                product.setStock(stock);
                productRepository.save(product);
            } else {
                product.setStock(stock);
                productRepository.save(product);
            }

            cart.updateTotalAmount(product);
        }

        // Update the total amount of the cart

        cartRepository.save(cart);
        map.put("message", "Item added to the cart");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //
//    @Transactional
//    public ResponseEntity<Object> applyPromocode(String code, HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            map.put("message", "User Not Login");
//            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//        } else {
//            Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);
//            System.err.print(optionalPromoCode);
//            if (!optionalPromoCode.isPresent()) {
//                map.put("message", "No promo code is present");
//                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//            }else {
//            PromoCode promoCode = optionalPromoCode.get();
//
//            if (!promoCode.isValid()) {
//                map.put("message", "Promocode is inactive");
//                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//            }else{
//
//            Cart cart = cartRepository.findByUserId(user.getId());
//            if (cart == null) {
//                map.put("message", "No cart found for the user" + user.getName());
//                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
//            } else {
//                if (promoCode.getType() == PromoCodeType.ORDER_BASED) {
//                    // Apply the discount to the total amount of the cart
//                    cart.setTotalAmount(cart.getTotalAmount() - promoCode.getDiscountValue());
//                } else if (promoCode.getType() == PromoCodeType.PRODUCT_BASED) {
//                    // Apply the discount to the specific product
//                    cart.getCartItems().stream()
//                            .filter(item -> {
//                                // Fetch the Product by productId (assuming you have a method to get it)
//                                Product product = productRepository.findById(item.getProductId()).get();
//                                // Compare the product name with the promo code's product name
//                                return product != null && product.getName().equals(promoCode.getProductName());
//                            })
//                            .forEach(item -> {
//                                // Fetch the Product again for price calculation
//                                Product product = productRepository.findById(item.getProductId()).get();
//                                if (product != null) {
//                                    // Apply discount based on the product price and promo code discount value
//                                    Double discount = product.getPrice() * promoCode.getDiscountValue() / 100;
//
//                                    // Update the total amount of the cart after applying the discount
//                                    cart.setTotalAmount(cart.getTotalAmount() - discount); // Subtract the discount from the total amount
//                                }
//                            });
//                }
//                cartRepository.save(cart);
//                map.put("message", "Prmocode is applied ");
//                return new ResponseEntity<>(map, HttpStatus.OK);
//            }
//
//            }
//        }
//
//    }
//}
    @Transactional
    public ResponseEntity<Object> applyPromocode(String code, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            map.put("message", "User Not Login");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        } else {
            Optional<PromoCode> optionalPromoCode = promoCodeRepository.findByCode(code);
            if (!optionalPromoCode.isPresent()) {
                map.put("message", "No promo code is present");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }

            PromoCode promoCode = optionalPromoCode.get();

            if (!promoCode.isValid()) {
                map.put("message", "Promocode is inactive");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }

            Cart cart = cartRepository.findByUserId(user.getId());
            if (cart == null) {
                map.put("message", "No cart found for the user " + user.getName());
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            } else {
                double totalDiscount = 0.0;

                if (promoCode.getType() == PromoCodeType.ORDER_BASED) {
                    // Apply the discount to the total amount of the cart
                    totalDiscount = promoCode.getDiscountValue();
                } else if (promoCode.getType() == PromoCodeType.PRODUCT_BASED) {
                    for (CartItem item : cart.getCartItems()) {
                        Optional<Product> productOpt = productRepository.findById(item.getProductId());
                        if (productOpt.isPresent()) {
                            Product product = productOpt.get();
                            if (product.getName().equals(promoCode.getProductName())) {
                                double discount = product.getPrice() * promoCode.getDiscountValue() / 100;
                                totalDiscount += discount;
                            }
                        }
                    }
                }

                // Apply total discount to the cart
                cart.setTotalAmount(cart.getTotalAmount() - totalDiscount);
                cartRepository.save(cart);

                map.put("message", "Promocode is applied successfully");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> checkouts(HttpSession session) throws ProductException {
        User user = (User) session.getAttribute("user");
        Cart cart = cartRepository.findByUserIdAndStatus(user.getId(), "active");
        if (cart == null) {
            map.put("message", "No active cart found for the curent user");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            map.put("message", "No active wallet found for the current user");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        // Check if the wallet has sufficient balance
        if (wallet.getBalance() < cart.getTotalAmount()) {
            // Insufficient funds, log failed transaction and inform user
            checkOutMethods.logFailedTransaction(user.getId(), cart.getTotalAmount());
            map.put("message", "No sufficient balance in the Wallet");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else {

            // Proceed with payment (deduct wallet balance)
            double previousBalance = wallet.getBalance();
            wallet.setBalance(wallet.getBalance() - cart.getTotalAmount());
            walletRepository.save(wallet);

            // Log wallet audit for the deduction
            checkOutMethods.logWalletAudit(user.getId(), cart.getTotalAmount(), wallet.getBalance(), "payment");

            // Create the order
            Orders order = checkOutMethods.createOrder(user.getId(), cart, cart.getTotalAmount());
            orderRepository.save(order);

            // Log order audit for the status change (from "Pending" to "Paid")
            checkOutMethods.logOrderAudit(order.getId(), "PENDING", "PAID");

            // Log a successful transaction and associate it with the order
            try {
                checkOutMethods.logSuccessfulTransaction(user.getId(), cart.getTotalAmount(), order.getId());
            } catch (ProductException e) {
                map.put("message", "order Not found");
                return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
            }
            cart.setStatus("Completed");
            cartRepository.save(cart);
            map.put("message", "Order successfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }


    public ResponseEntity<Object> fetchProducts(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            map.put("message", "User Not Login");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        } else {
            List<Product> productList=productRepository.findByIsDeletedFalseAndStockGreaterThan(1);
            for (Product product : productList) {
                // Assuming getCategory() returns a Category object with a name attribute
                System.out.println("Product Name: " + product.getCategory());
            }
            if(productList.isEmpty())
            {
                map.put("message","No product present in the shope");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }else {
                map.put("data",productList);
                map.put("message","Product List");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }
    }
}

