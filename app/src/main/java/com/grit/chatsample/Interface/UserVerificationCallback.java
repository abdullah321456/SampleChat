package com.grit.chatsample.Interface;

import com.grit.chatsample.pojos.Users;

public interface UserVerificationCallback {
    public void handleVerification(boolean success, String message, Users user);
}
