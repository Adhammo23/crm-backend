package com.adham.crm_backend.security;

import com.adham.crm_backend.entity.User;

public interface HasOwner {
    User getOwner();
}
