package ru.klokov.onlineexam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.klokov.onlineexam.exception.ResourceAlreadyExistsException;
import ru.klokov.onlineexam.exception.ResourceNotFoundException;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.model.Subcategory;
import ru.klokov.onlineexam.repository.SubcategoryRepository;
import ru.klokov.onlineexam.service.SubcategoryService;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;

    @Override
    public List<Subcategory> getAllSubcategories() { return subcategoryRepository.findAll(); }

    @Override
    public Page<Subcategory> getAllSubcategoriesPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return subcategoryRepository.findAll(pageable);
    }

    @Override
    public Subcategory getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with ID = " + id));
    }

    @Override
    public Subcategory createSubcategory(Subcategory subcategory) {
        if (subcategoryAlreadyExists(subcategory))
            throw new ResourceAlreadyExistsException("Subcategory with name " + subcategory.getName() + " already exists!");
        return subcategoryRepository.save(subcategory);
    }

    @Override
    public Subcategory editSubcategory(Subcategory newSubcategory, Long subcategoryToUpdateId) {
        if (subcategoryAlreadyExists(newSubcategory))
            throw new ResourceAlreadyExistsException("Subcategory with name " + newSubcategory.getName() + " already exists!");

        Subcategory subcategoryToUpdate = subcategoryRepository.findById(subcategoryToUpdateId)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with ID = " + subcategoryToUpdateId));

        subcategoryToUpdate.setName(newSubcategory.getName());
        subcategoryToUpdate.setDescription(newSubcategory.getDescription());
        // TODO set tests

        return subcategoryRepository.save(subcategoryToUpdate);
    }

    @Override
    public void deleteSubcategoryById(Long id) {
        subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with ID = " + id));
        subcategoryRepository.deleteById(id);
    }

    @Override
    public Boolean subcategoryAlreadyExists(Subcategory subcategory) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Example<Subcategory> example = Example.of(subcategory, matcher);
        return subcategoryRepository.exists(example);
    }
}
