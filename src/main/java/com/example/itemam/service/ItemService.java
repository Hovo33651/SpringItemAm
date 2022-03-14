package com.example.itemam.service;

import com.example.itemam.dto.CreateItemRequest;
import com.example.itemam.entity.Item;
import com.example.itemam.entity.ItemImage;
import com.example.itemam.exception.ItemNotFoundException;
import com.example.itemam.repository.CategoryRepository;
import com.example.itemam.repository.ItemImageRepository;
import com.example.itemam.repository.ItemRepository;
import com.example.itemam.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {


    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemImageRepository itemImageRepository;

    @Value("${itemAm.upload.path}")
    String imagePath;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }


    public List<Item> findItemsByCategoryId(int id) {
        return itemRepository.findItemsByCategory_Id(id);
    }

    public Item findItemById(int id) throws ItemNotFoundException {
        Optional<Item> byId = itemRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ItemNotFoundException();
        }
        return byId.get();
    }

    public List<Item> findItemsByUserId(int id) {
        return itemRepository.findItemsByUserId(id);
    }



    public void saveItemFromItemRequest(CreateItemRequest createItemRequest, MultipartFile[] uploadedFiles, CurrentUser currentUser) throws IOException {
        Item item = getItemFromItemRequest(createItemRequest, currentUser);
        itemRepository.save(item);
        saveItemImages(uploadedFiles, item);

    }

    private void saveItemImages(MultipartFile[] uploadedFiles, Item item) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                if(Objects.equals(uploadedFile.getOriginalFilename(), "")){
                    return;
                }
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(imagePath + fileName);
                uploadedFile.transferTo(newFile);
                ItemImage image = ItemImage.builder()
                        .url(fileName)
                        .item(item)
                        .build();
                itemImageRepository.save(image);
            }
        }
    }


    private Item getItemFromItemRequest(CreateItemRequest createItemRequest, CurrentUser currentUser) {
        return Item.builder()
                .title(createItemRequest.getTitle())
                .description(createItemRequest.getDescription())
                .price(createItemRequest.getPrice())
                .currency(createItemRequest.getCurrency())
                .category(categoryRepository.findById(createItemRequest.getCategoryId()).get())
                .user(currentUser.getUser())
                .build();
    }

    public void deleteItemById(int id) {
        itemImageRepository.deleteItemImageByItemId(id);
        itemRepository.deleteById(id);
    }
}
