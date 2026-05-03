package com.merouane.InventorySystem.services.Implementation;

import com.merouane.InventorySystem.dtos.LoginRequest;
import com.merouane.InventorySystem.dtos.RegisterRequest;
import com.merouane.InventorySystem.dtos.Response;
import com.merouane.InventorySystem.dtos.UserDTO;
import com.merouane.InventorySystem.enums.UserRole;
import com.merouane.InventorySystem.exceptions.InvalidCredentialsException;
import com.merouane.InventorySystem.models.User;
import com.merouane.InventorySystem.repositories.UserRepository;
import com.merouane.InventorySystem.security.JwtUtils;
import com.merouane.InventorySystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {



    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private User user;


    @Override
    public Response registerUser(RegisterRequest request) {
        UserRole role = UserRole.MANAGER;
        if (request.getRole() != null) {
            role  = request.getRole();
        }

        User userToSave = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);


        return Response.builder()
                .status(200)
                .message("The user was successfully created")
                .build();

    }



    @Override
    public Response loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email Not Found"));

        log.info(String.valueOf(user));
        if (!passwordEncoder.matches(request.getPassword() , user.getPassword())){throw new InvalidCredentialsException("Password does not match");}

        String token  = jwtUtils.generateToken(user.getEmail());

        return Response.builder().
                status(200)
                .message("User logged in successfully ")
                .role(user.getRole())
                .token(token)
                .expirationDate("6 months")
                .build();





    }

    @Override
    public Response getAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC , "id"));
        userList.forEach(user->user.setTransactions(null));


        List<UserDTO> userDTOS = modelMapper.map(
                userList ,
                new TypeToken<List<UserDTO>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success!")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =  userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        user.setTransactions(null);
        return user;

    }

    @Override
    public Response getUserById(Long id) {
        User user;
        user = userRepository.findById(id).orElseThrow(()->
                new UsernameNotFoundException("The user wasn't found")
        );


        UserDTO userDTO = modelMapper.map(user , UserDTO.class);
        user.setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success!")
                .user(userDTO)
                        .build();
    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {

        Optional<User> user = userRepository.findById(id);
        if (userDTO.getEmail() !=null){
            user.get().setEmail(userDTO.getEmail());
        }if (userDTO.getPhoneNumber() !=null){
            user.get().setPhoneNumber(userDTO.getPhoneNumber());
        }if (userDTO.getName() !=null){
            user.get().setName(userDTO.getName());
        }if (userDTO.getRole() !=null){
            user.get().setRole(userDTO.getRole());
        }if (userDTO.getPassword() !=null && !userDTO.getPassword().isBlank()){
            user.get().setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user.get());
        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }


    @Override
    public Response deleteUser(Long id) {
        userRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("User Deleted successfully!")
                .build();

    }

    @Override
    public Response getUserTransactions(Long id) {

        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = modelMapper.map(user , UserDTO.class);
        userDTO.getTransactions().forEach(transactionDTO -> {

            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);

        });
        return Response.builder()
                .status(200)
                .message("success!")
                .user(userDTO)
                .build();
    }
}
