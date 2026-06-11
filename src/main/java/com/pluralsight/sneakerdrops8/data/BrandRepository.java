package com.pluralsight.sneakerdrops8.data;

import com.pluralsight.sneakerdrops8.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByName(String name);
}