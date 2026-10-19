package com.example.democoming.service;
import com.example.democoming.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.HashMap;

@Service
public class Userservice {
    private Map<String, User> users = new HashMap<>();
    @Value("${app.auth.user-file}")
    private String userFile;
    @PostConstruct
    public void ScannerUsers() throws IOException {
        Path path = Path.of(userFile);
        if (!Files.exists(path)) {
            return;
        }
        for (String line : Files.readAllLines(path)) {

            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\t", 2);

            if (parts.length != 2) {
                throw new IOException("用户文件格式错误");
            }
            User user = new User();
            user.setName(parts[0]);
            user.setPassword(parts[1]);
            users.put(user.getName(), user);
        }
    }
    public void save(User user) throws IOException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!users.containsKey(user.getName())) {
            user.setPassword(encoder.encode(user.getPassword()));
            Path path = Path.of(userFile);
            Files.createDirectories(path.getParent());
            String List = user.getName() + "\t" + user.getPassword() + System.lineSeparator();
            Files.writeString(path,
                    List,
                    StandardOpenOption.
                            CREATE,
                    StandardOpenOption.
                            APPEND);
            users.put(user.getName(), user);
        }
    }

    public User findUser(String username) {
        return users.get(username);
    }
    public void usercheck(User user){
        if(user.getName()==null||user.getName().isBlank())
        {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if(user.getPassword()==null||user.getPassword().isBlank())
        {
            throw new IllegalArgumentException("密码不能为空");
        }

    }

    @Value("${app.auth.min-password-length}")
    private int minPasswordLength;
    @Value("${app.auth.username-pattern}")
    private String usernamePattern;
    @Value("${app.auth.max-password-length}")
    private int maxPasswordLength;
    @Value("${app.auth.userpassword-pattern}")
    private String passwordPattern;

    public void registeruser(User user) {
        usercheck(user);
        if (user.getPassword().length() < minPasswordLength) {
            throw new IllegalArgumentException("密码的最小长度应为" + minPasswordLength + ",请重新输入");
        }
        if (!user.getName().matches(usernamePattern)) {
            throw new IllegalArgumentException("用户名不符合规则：" + usernamePattern);
        }
        if (user.getPassword().length() > maxPasswordLength) {
            throw new IllegalArgumentException("密码的最大长度应为" + maxPasswordLength + ",请重新输入");
        }
        if (!user.getPassword().matches(passwordPattern)) {
            throw new IllegalArgumentException("密码不符合规范：" + passwordPattern);
        }
    }
    public String register(User user)throws IOException{
        registeruser(user);
        User user_check2 = findUser(user.getName());
        if (user_check2 == null) {
            save(user);
            return "register success";
        } else {
            return "用户名已存在";
        }

    }
    public String login(User user) {
        usercheck(user);
        User user_check1 = findUser(user.getName());
        if (user_check1 == null) {
            return "用户不存在";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(
                user.getPassword(), user_check1.getPassword())
        ) {
            return "login success";
        }
        return "密码错误";
    }
}

