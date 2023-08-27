package ru.andreybaryshnikov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.andreybaryshnikov.models.PhoneBook;

public interface PhonebookRepository extends CrudRepository<PhoneBook, Integer> {
}
