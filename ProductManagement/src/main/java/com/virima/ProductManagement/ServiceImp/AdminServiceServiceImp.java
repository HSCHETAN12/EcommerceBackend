package com.virima.ProductManagement.ServiceImp;


import com.virima.ProductManagement.Entity.*;
import com.virima.ProductManagement.Helper.AES;
import com.virima.ProductManagement.Helper.CloudinaryHelper;
import com.virima.ProductManagement.Repository.*;
import com.virima.ProductManagement.Service.AdminService;
import com.virima.ProductManagement.dto.UserDTO;
import com.virima.ProductManagement.dto.WalletTopUpRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class AdminServiceServiceImp implements AdminService {
    @Autowired
    AdminRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CloudinaryHelper cloudinaryHelper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletAuditRepository walletAuditRepository;

    @Autowired
    PromoCodeRepository promoCodeRepository;

    Map<String,Object> map=new HashMap<>();

    public ResponseEntity<Object> save( Admin admin, BindingResult bindingResult) {
        // Check for validation errors


        if (bindingResult.hasErrors()) {
            // Collect all the error messages
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errors.append(error.getDefaultMessage()).append("\n");
            });

            map.put("message",errors);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // If no validation errors, save the product
        else {
            admin.setPassword(AES.encrypt(admin.getPassword()));

            repository.save(admin);
            Map<String,Object> map=new HashMap<>();
            map.put("message","Admin added successfully");
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> verify(Admin admin, HttpSession session) {
        Admin admin1 = repository.findByName(admin.getName());
        if (admin1 != null && AES.encrypt(admin.getPassword()).equals(admin1.getPassword())) {
            session.setAttribute("admin", admin1);  // Store admin in session
            map.put("message", "Login Successful");
           System.out.print( session.getAttribute("admin"));
            return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
        } else {
            map.put("message", "Invalid login credentials");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }







    public ResponseEntity<Object> saveProduct1(String name, String description, double price, int stock, String category, MultipartFile image, HttpSession session) {
        Admin admin1=(Admin)session.getAttribute("admin");
        if(admin1==null)
        {
            session.setAttribute("fail", "Invaild Admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }else{
                Product product=new Product();
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                product.setDescription(description);
                product.setStock(stock);
                if(stock>=1)
                {
                    product.setStatus("Available");
                }else{
                    product.setStatus("Not Avaliable");
                }
                product.setCreatedBy(admin1.getName());
                product.setUpdatedBy(admin1.getName());
                product.setImageUrl(cloudinaryHelper.saveImage(image));
                productRepository.save(product);
                map.put("message","Product added Successfully");
                return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
        }
    }


    public ResponseEntity<Object> fetchProducts(HttpSession session) {
        Admin admin1=(Admin)session.getAttribute("admin");
        if(admin1==null)
        {
            session.setAttribute("fail", "Invaild admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
        }else{
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

    public ResponseEntity<Object> fetchUser(HttpSession session) {
        Admin admin=(Admin)session.getAttribute("admin");
        if(admin==null)
        {
            session.setAttribute("fail", "Invaild admin");
            map.put("message","No admin found");
            return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
        }else {
            List<User> users = userRepository.findVerifiedAndNotDeleted();

            if (users.isEmpty()) {
                map.put("message", "No verified users exist in the database");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {

                List<UserDTO> userDetails = new ArrayList<>();

                for (User user : users) {
                    // Creating a UserDTO for each user
                    UserDTO userDTO = new UserDTO(
                            user.getId(),
                            user.getName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getWallet().getBalance(),
                            user.getMobile(),
                            user.getGender()
                    );

                    userDetails.add(userDTO);
                }

                map.put("data", userDetails); // Adding the user details to the response
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
    }


    public ResponseEntity<Object> fetchByCategory(String category, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            List<Product> productList = productRepository.findByCategory(category);
            if (productList.isEmpty()) {
                map.put("message", "category not found");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                map.put("data", productList);
                map.put("message", "the Product List based on the category");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
    }


    public ResponseEntity<Object> updateProduct(int id, Product product, HttpSession session) {
        Admin admin=(Admin)session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            Optional<Product> product1=productRepository.findById(id);
            if(product1==null)
            {
                map.put("message","Products Not found");
                return new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
            }
            else {
                Product existingProduct=product1.get();
                if(product.getName()!=null)
                    existingProduct.setName(product.getName());
                if(product.getDescription()!=null)
                    existingProduct.setDescription(product.getDescription());
                if(product.getPrice()!=0)
                    existingProduct.setPrice(product.getPrice());
                if(product.getStock()!=0)
                    existingProduct.setStock(product.getStock());
                if(product.getCategory()!=null)
                    existingProduct.setCategory(product.getCategory());
                if(product.getStatus()!=null)
                    existingProduct.setStatus(product.getStatus());
                if(product.getImageUrl()!=null)
                    existingProduct.setImageUrl(product.getImageUrl());
                productRepository.save(existingProduct);
                map.put("message","Product get updated");
                return new ResponseEntity<Object>(map,HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> logOut(HttpSession session) {
            session.removeAttribute("admin");
            session.setAttribute("message", "Logout sucessfully");
            map.put("message","LogoutSuccessfull");
            return new ResponseEntity<>(map,HttpStatus.OK);
    }

    public ResponseEntity<Object> fetchByName(String name, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            List<Product> productList=productRepository.findByName(name);
            if(productList.isEmpty())
            {
                map.put("message","Product Not Found by the name");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }else {
                map.put("message", "ProductList with the name");
                map.put("data", productList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }

    }

    public ResponseEntity<Object> WalletTopUp(WalletTopUpRequest walletTopUpRequest, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            Wallet wallet = walletRepository.findByUserId(walletTopUpRequest.getUserId());
            if (wallet == null) {
                map.put("message","User's wallet not found");
                return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
            }
            double newBalance = wallet.getBalance() + walletTopUpRequest.getAmount();
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);

            WalletAudit walletAudit = new WalletAudit();
            walletAudit.setUserId(walletTopUpRequest.getUserId());
            walletAudit.setTransactionType("CREDIT");
            walletAudit.setAmount(walletTopUpRequest.getAmount());
            walletAudit.setBalanceAfterTransaction(newBalance);
            walletAudit.setTransactiondate(LocalDateTime.now());
            walletAuditRepository.save(walletAudit);
            map.put("message","Wallet topup done to given user id");
            map.put("data",walletTopUpRequest);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
    }


    public ResponseEntity<Object> createPromoCode(String code, Double discountValue, PromoCodeType type, LocalDateTime startDate, LocalDateTime endDate, PromoCodeStatus status, String productName, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            PromoCode promoCode = new PromoCode(code, discountValue, type, startDate, endDate, status, productName);
            promoCodeRepository.save(promoCode);
            map.put("message","Promocode Added successfully");
            map.put("data",promoCode);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> UpdatePromoCode(int id,PromoCode promoCode, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            PromoCode promoCode1=promoCodeRepository.findById(id).get();
            if(promoCode1==null)
            {
                map.put("message","No promocode is present in the given id");
                return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
            }else{
                PromoCode existingPromoCode=promoCode1;

                // Update only the fields that are not null in the patchRequest
                if (promoCode.getCode() != null) {
                    existingPromoCode.setCode(promoCode.getCode());
                }
                if (promoCode.getDiscountValue() != null) {
                    existingPromoCode.setDiscountValue(promoCode.getDiscountValue());
                }
                if (promoCode.getType() != null) {
                    existingPromoCode.setType(promoCode.getType());
                }
                if (promoCode.getStartDate() != null) {
                    existingPromoCode.setStartDate(promoCode.getStartDate());
                }
                if (promoCode.getEndDate() != null) {
                    existingPromoCode.setEndDate(promoCode.getEndDate());
                }
                if (promoCode.getStatus() != null) {
                    existingPromoCode.setStatus(promoCode.getStatus());
                }
                if (promoCode.getProductName() != null) {
                    existingPromoCode.setProductName(promoCode.getProductName());
                }

                promoCodeRepository.save(existingPromoCode);
                map.put("message","Promocode updated successfully");
                map.put("data",promoCode);
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }
    }

    public ResponseEntity<Object> fetchWalletAudict(int user, HttpSession session) {
        Admin admin=(Admin)session.getAttribute("admin");
        if (admin == null) {
            session.setAttribute("fail", "Invaild admin");
            map.put("message", "No admin found");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } else {
            List<WalletAudit> usersAudict=walletAuditRepository.findByUserId(user);
            if(usersAudict.isEmpty())
            {
                map.put("message","No user wallet tranaction found");
                return new ResponseEntity<>(map,HttpStatus.OK);
            }else {
                map.put("message","user wallet tranaction are");
                map.put("data",usersAudict);
                return new ResponseEntity<>(map,HttpStatus.OK);
            }
        }
    }

//    public ResponseEntity<Object> daletUser(int id, HttpSession session) {
//        Admin admin=(Admin)session.getAttribute("admin");
//        if (admin == null) {
//            session.setAttribute("fail", "Invaild admin");
//            map.put("message", "No admin found");
//            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
//        } else {
//            User user=userRepository.find
//        }
//    }
}

