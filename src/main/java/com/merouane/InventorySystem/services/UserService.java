package com.merouane.InventorySystem.services;


import com.merouane.InventorySystem.dtos.LoginRequest;
import com.merouane.InventorySystem.dtos.RegisterRequest;
import com.merouane.InventorySystem.dtos.Response;
import com.merouane.InventorySystem.dtos.UserDTO;
import com.merouane.InventorySystem.models.User;

public interface UserService {

    Response registerUser(RegisterRequest request);

    Response loginUser(LoginRequest request);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById( Long id);

    Response updateUser(Long id , UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);


}
