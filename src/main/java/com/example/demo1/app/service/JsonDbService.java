package com.example.demo1.app.service;
import com.example.demo1.app.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

//JsonDbService → user data JSON file এ save, load, update, login verify করে।
public class JsonDbService {
    private static final String FILE_PATH = "src/main/resources/data/user_data.json";//ata holo file path

    private final ObjectMapper mapper = new ObjectMapper();
    private final File databaseFile;
    
    // object create hole automatically check korbe je file ache kina, jodi na thake tahole folder create korbe, empty user list save korbe, ebong default admin create korbe.
    public JsonDbService() throws IOException {
        this.databaseFile = new File(FILE_PATH);
        if (!databaseFile.exists() || databaseFile.length() == 0) {
            databaseFile.getParentFile().mkdirs();
            saveUsers(new ArrayList<>());
            createDefaultAdmin();
        }
    }
// default admin user create korar method, jodi admin user na thake tahole create korbe.
    private void createDefaultAdmin() throws IOException {
        List<User> users = loadUsers();
        if (users.stream().noneMatch(u -> "admin".equals(u.getUsername()))) {
            String adminHash = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";
            users.add(new User("Admin User", "admin@example.com", "01700000000", "admin", adminHash));
            saveUsers(users);
        }
    }
// user data load korar method, jodi file na thake ba file empty thake tahole empty list return korbe.
    public List<User> loadUsers() throws IOException {
        if (!databaseFile.exists() || databaseFile.length() == 0) {
            return new ArrayList<>();
        }
        return mapper.readValue(databaseFile, new TypeReference<List<User>>() {});
    }
// user data save korar method, jodi file na thake tahole file create korbe, ebong user list save korbe.
    private void saveUsers(List<User> users) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(databaseFile, users);
    }
    // user registration method, jodi user already exist kore tahole false return korbe, na thakle user add kore true return korbe.
    public boolean registerUser(User newUser) throws IOException {
        List<User> users = loadUsers();
        boolean exists = users.stream().anyMatch(u -> u.getMobile().equals(newUser.getMobile()) || u.getUsername().equalsIgnoreCase(newUser.getUsername()));
        if (exists) {
            return false;
        }
        users.add(newUser);
        saveUsers(users);
        return true;
    }
// user update method, jodi user exist kore tahole user update kore true return korbe, na thakle false return korbe.
    public boolean updateUser(User updatedUser) throws IOException {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            User existing = users.get(i);
            if (existing.getUsername() != null
                    && updatedUser.getUsername() != null
                    && existing.getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                saveUsers(users);
                return true;
            }
        }
        return false;
    }
// user login method, jodi user exist kore tahole user return korbe, na thakle null return korbe.
    public User loginUser(String input, String rawPassword) throws IOException {
        List<User> users = loadUsers();
        String hashedPassword = hashPassword(rawPassword);

        return users.stream()
                .filter(u -> (u.getMobile().equals(input) || u.getUsername().equals(input) || u.getEmail().equals(input) || u.getName().equalsIgnoreCase(input))
                        && u.getPasswordHash().equals(hashedPassword))
                .findFirst()
                .orElse(null);
    }
// password hash korar method, SHA-1 algorithm use kore password hash kore return korbe.
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
