package me.pawel.openshiftgs.repositories;

import me.pawel.openshiftgs.model.Fruit;
import org.springframework.data.repository.CrudRepository;

public interface FruitRepository extends CrudRepository<Fruit, Integer> {
}
