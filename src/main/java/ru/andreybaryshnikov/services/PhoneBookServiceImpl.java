package ru.andreybaryshnikov.services;

import ru.andreybaryshnikov.repository.PhonebookRepository;
import ru.andreybaryshnikov.models.PhoneBook;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class PhoneBookServiceImpl implements PhoneBookService{
    private PhonebookRepository phoneBookRepository;

    public PhoneBookServiceImpl(PhonebookRepository phoneBookRepository) {
        this.phoneBookRepository = phoneBookRepository;
    }

    @Override
    public PhoneBook getPhoneBook(int id) {
        return phoneBookRepository.findById(id).get();
    }
    @Override
    public Iterable<PhoneBook> getPhoneBooks() {
        Iterable<PhoneBook> phoneBooks = phoneBookRepository.findAll();
        if (((Collection<PhoneBook>) phoneBooks).size() == 0){
            phoneBookRepository.save(new PhoneBook("Ivanov", "Ivan", "Ivanovich", "+7(919)154-56-78",
                "st. Lenina 1, 15", "worker"));
            phoneBookRepository.save(new PhoneBook("Petrov", "Petr", "Petrovich",
                "+7(919)154-56-79", "st. Petrova 7, 3", "worker"));
            phoneBookRepository.save(new PhoneBook("Sidorov", "Sidor", "Sidorovich",
                "+7(919)154-56-80", "st. Pushkina 4, 1", "worker"));
            phoneBooks = phoneBookRepository.findAll();
        }
        return phoneBooks;
    }
    @Override
    public void editRecordToPhoneBooks(PhoneBook newPhoneBook){
        phoneBookRepository.save(newPhoneBook);
    }
    @Override
    public void deleteRecordToPhoneBooks(int id){
        phoneBookRepository.deleteById(id);
    }
    @Override
    public PhoneBook getNewPhoneBook() {
        return new PhoneBook();
    }
    @Override
    public void addRecordToPhoneBooks(PhoneBook newPhoneBook) {
        phoneBookRepository.save(newPhoneBook);
    }
}
