package ma.project.GedforSaas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	void deleteSubscriptionById(Long id);
	Optional<Subscription> findSubscriptionById(Long id);
	Subscription findByUserId(Long id);
	List<Subscription> findByCompanyId(Long id);
	List<Subscription> findByClientSecretAndReference(String clientSecret, String reference);
}
