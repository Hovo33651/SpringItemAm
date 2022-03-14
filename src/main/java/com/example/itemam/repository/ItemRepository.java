package com.example.itemam.repository;

import com.example.itemam.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findItemsByCategory_Id(int id);

    List<Item> findItemsByUserId(int id);
}
