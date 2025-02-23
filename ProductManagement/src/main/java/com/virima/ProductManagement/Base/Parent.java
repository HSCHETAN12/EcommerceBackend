package com.virima.ProductManagement.Base;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

public class Parent {

    boolean isDeleted;
    boolean idArchived;
    @CreationTimestamp
    Date createdAt;
    @UpdateTimestamp
    Date updatedAt;
    String createdBy;
    String updatedBy;
}
