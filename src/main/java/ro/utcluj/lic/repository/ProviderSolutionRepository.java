package ro.utcluj.lic.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ro.utcluj.lic.domain.ProviderSolution;

public interface ProviderSolutionRepository extends MongoRepository<ProviderSolution, ObjectId> {

}
