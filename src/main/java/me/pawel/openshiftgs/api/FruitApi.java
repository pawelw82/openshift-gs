package me.pawel.openshiftgs.api;

import me.pawel.openshiftgs.model.Fruit;
import me.pawel.openshiftgs.repositories.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping(value = "/api/fruits")
public class FruitApi {

    private final FruitRepository repository;

    @Autowired
    public FruitApi(FruitRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List getAll() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Fruit post(@RequestBody(required = false) Fruit fruit) {
        verifyCorrectPayload(fruit);

        return repository.save(fruit);
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Fruit get(@PathVariable("id") Integer id) {
        verifyFruitExists1(id);

        return repository.findById(id).orElse(null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Fruit put(@PathVariable("id") Integer id, @RequestBody(required = false) Fruit fruit) {
        verifyFruitExists1(id);
        verifyCorrectPayload(fruit);

        fruit.setId(id);
        return repository.save(fruit);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        verifyFruitExists1(id);

        repository.deleteById(id);
    }

    private void verifyFruitExists1(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(String.format("Fruit with id=%d was not found", id));
        }
    }

    private void verifyCorrectPayload(Fruit fruit) {
        if (Objects.isNull(fruit)) {
            throw new RuntimeException("Fruit cannot be null");
        }

        if (!Objects.isNull(fruit.getId())) {
            throw new RuntimeException("Id field must be generated");
        }
    }
}
