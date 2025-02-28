package com.virima.ProductManagement.Base;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@MappedSuperclass
@Data
public class Parent {

   public boolean isDeleted;
    public boolean isArchived;
    @CreationTimestamp
    public  Date createdAt;
    @UpdateTimestamp
    public Date updatedAt;
    public String createdBy;
    public String updatedBy;
}
