package com.example.itemam.repository;

import com.example.itemam.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ItemImageRepository extends JpaRepository<ItemImage,Integer> {

    @Transactional
    void deleteItemImageByItemId(int id);
}
