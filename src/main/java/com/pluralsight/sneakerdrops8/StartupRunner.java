package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final SneakerRepository sneakerRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public StartupRunner(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public void run(String... args) {
        seedData();
        Scanner myScanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Sneaker Drops ===");
            System.out.println("1) List all sneakers");
            System.out.println("2) Search by model");
            System.out.println("3) Find by price");
            System.out.println("4) Find by release year");
            System.out.println("5) Advanced search");
            System.out.println("6) View by id");
            System.out.println("7) Add a sneaker");
            System.out.println("8) Update price");
            System.out.println("9) Delete a sneaker");
            System.out.println("0) Quit");
            System.out.print("Choose: ");

            switch (myScanner.nextInt()) {
                case 1 -> listSneakers();
                case 2 -> findByModel(myScanner);
                case 3 -> findByPrice(myScanner);
                case 4 -> findByYear(myScanner);
                case 5 -> advancedSearch(myScanner);
                case 6 -> viewById(myScanner);
                case 7 -> addSneaker(myScanner);
                case 8 -> updatePrice(myScanner);
                case 9 -> deleteSneaker(myScanner);
                case 0 -> running = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void listSneakers() {
        System.out.println("You have " + sneakerRepository.count() + " sneakers:");
        for (Sneaker s : sneakerRepository.findAll()) {
            System.out.println(s.getId() + " - " + s.getModel() + " (" + s.getPrice() + ")");
        }
    }

    private void findByModel(Scanner myScanner) {
        myScanner.nextLine(); // clear leftover newline
        System.out.print("Model contains: ");
        String text = myScanner.nextLine();
        List<Sneaker> results = sneakerRepository.findByModelContaining(text);
        for (Sneaker s : results) {
            System.out.println(s.getModel() + " (" + s.getPrice() + ")");
        }
    }

    private void findByPrice(Scanner myScanner) {
        System.out.print("Maximum price: ");
        double max = myScanner.nextDouble();
        List<Sneaker> results = sneakerRepository.findByPriceLessThan(max);
        for (Sneaker s : results) {
            System.out.println(s.getModel() + " (" + s.getPrice() + ")");
        }
    }

    private void findByYear(Scanner myScanner) {
        System.out.print("Year: ");
        int year = myScanner.nextInt();
        List<Sneaker> results = sneakerRepository.findByReleaseYear(year);
        for (Sneaker s : results) {
            System.out.println(s.getModel() + " (" + s.getReleaseYear() + ")");
        }
    }

    private void advancedSearch(Scanner myScanner) {
        System.out.print("Maximum price: ");
        double maxPrice = myScanner.nextDouble();
        System.out.print("Released on or after year: ");
        int minYear = myScanner.nextInt();
        List<Sneaker> results = sneakerRepository.search(maxPrice, minYear);
        for (Sneaker s : results) {
            System.out.println(s.getModel() + " (" + s.getPrice() + ", " + s.getReleaseYear() + ")");
        }
    }

    private void viewById(Scanner myScanner) {
        System.out.print("Sneaker id: ");
        int id = myScanner.nextInt();
        Sneaker sneaker = sneakerRepository.findById((long) id).orElse(null);
        if (sneaker == null) {
            System.out.println("No sneaker with that id.");
        } else {
            System.out.println(sneaker.getId() + " - " + sneaker.getModel() + " (" + sneaker.getPrice() + ")");
        }
    }

    private void addSneaker(Scanner myScanner) {
        myScanner.nextLine(); // clear leftover newline
        System.out.print("Model: ");
        String model = myScanner.nextLine();
        System.out.print("Price: ");
        double price = myScanner.nextDouble();
        System.out.print("Release year: ");
        int year = myScanner.nextInt();
        Sneaker sneaker = new Sneaker(model, price, year);
        sneakerRepository.save(sneaker);
        System.out.println("Saved: " + sneaker.getId() + " - " + sneaker.getModel());
    }

    private void updatePrice(Scanner myScanner) {
        System.out.print("Sneaker id: ");
        int id = myScanner.nextInt();
        Sneaker sneaker = sneakerRepository.findById((long) id).orElse(null);
        if (sneaker == null) {
            System.out.println("No sneaker with that id.");
        } else {
            System.out.print("New price: ");
            double newPrice = myScanner.nextDouble();
            sneaker.setPrice(newPrice);
            sneakerRepository.save(sneaker);
            System.out.println("Updated: " + sneaker.getModel() + " is now $" + sneaker.getPrice());
        }
    }

    private void deleteSneaker(Scanner myScanner) {
        System.out.print("Sneaker id: ");
        int id = myScanner.nextInt();
        if (!sneakerRepository.existsById((long) id)) {
            System.out.println("No sneaker with that id.");
        } else {
            sneakerRepository.deleteById((long) id);
            System.out.println("Deleted sneaker " + id + ".");
        }
    }

    private void seedData() {
        if (brandRepository.count() == 0) {
            brandRepository.save(new Brand("Nike"));
            brandRepository.save(new Brand("Adidas"));
            brandRepository.save(new Brand("New Balance"));
            brandRepository.save(new Brand("Puma"));
            brandRepository.save(new Brand("Reebok"));
        }
        if (sneakerRepository.count() == 0) {
            sneakerRepository.save(new Sneaker("Air Max 90", 129.99, 1990));
            sneakerRepository.save(new Sneaker("Ultraboost", 179.99, 2015));
            sneakerRepository.save(new Sneaker("574", 89.99, 1988));
            sneakerRepository.save(new Sneaker("Suede Classic", 74.99, 1968));
            sneakerRepository.save(new Sneaker("Club C 85", 79.99, 1985));
            sneakerRepository.save(new Sneaker("Air Force 1", 109.99, 1982));
            sneakerRepository.save(new Sneaker("Gazelle", 99.99, 1968));
            sneakerRepository.save(new Sneaker("Dunk Low", 119.99, 1985));
        }
    }
}