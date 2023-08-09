package com.kaj.mypet.auth;

import com.kaj.mypet.auth.entity.Profile;
import com.kaj.mypet.auth.entity.ProfileRepository;
import com.kaj.mypet.auth.entity.User;
import com.kaj.mypet.auth.entity.UserRepository;
import com.kaj.mypet.auth.util.HashUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepo;
    private ProfileRepository profileRepo;

    @Autowired
    private HashUtil hash;

    @Autowired
    public AuthService(UserRepository userRepo, ProfileRepository profileRepo){
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    @Transactional
    public void createIdentity(SignUpRequest req){
        User toSaveUser = User.builder()
                .userid(req.getUserId())
                .secret(hash.createHash(req.getPassword()))
                .nickname(req.getNickname())
                .build();

        User saveUser = userRepo.save(toSaveUser);

        Profile toSaveProfile = Profile.builder()
                .nickname(req.getNickname())
                .petname(req.getPetname())
                .species(req.getSpecies())
                .user(saveUser)
                .build();
        profileRepo.save(toSaveProfile);

    }


}
