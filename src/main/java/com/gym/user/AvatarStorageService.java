package com.gym.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE_BYTES = 5L * 1024L * 1024L;

    private final Path avatarDir;

    public AvatarStorageService(@Value("${app.upload.root:data/uploads}") String uploadRoot) {
        this.avatarDir = Paths.get(uploadRoot, "avatars").toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        validate(file);

        try {
            Files.createDirectories(avatarDir);
            String extension = resolveExtension(file);
            String filename = UUID.randomUUID() + extension;
            Path target = avatarDir.resolve(filename).normalize();

            if (!target.startsWith(avatarDir)) {
                throw new IllegalArgumentException("Neispravan naziv fajla.");
            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/avatars/" + filename;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Neuspešno čuvanje slike.");
        }
    }

    public void deleteIfManaged(String avatarUrl) {
        if (avatarUrl == null || !avatarUrl.startsWith("/uploads/avatars/")) {
            return;
        }

        String filename = avatarUrl.substring("/uploads/avatars/".length());
        if (filename.isBlank()) {
            return;
        }

        try {
            Path target = avatarDir.resolve(filename).normalize();
            if (target.startsWith(avatarDir)) {
                Files.deleteIfExists(target);
            }
        } catch (IOException ignored) {
            // If deleting old avatar fails, keep request successful and leave cleanup for later.
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fajl je obavezan.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Dozvoljeni formati su JPG, PNG i WEBP.");
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("Maksimalna veličina slike je 5MB.");
        }
    }

    private String resolveExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if ("image/png".equals(contentType)) {
            return ".png";
        }
        if ("image/webp".equals(contentType)) {
            return ".webp";
        }
        return ".jpg";
    }
}