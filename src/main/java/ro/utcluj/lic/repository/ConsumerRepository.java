package ro.utcluj.lic.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.utcluj.lic.domain.Consumer;

@Repository
public interface ConsumerRepository extends MongoRepository<Consumer, ObjectId> {
}
