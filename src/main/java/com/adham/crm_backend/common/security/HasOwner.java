package com.adham.crm_backend.common.security;

import com.adham.crm_backend.user.entity.User;

public interface HasOwner {
    User getOwner();
}
