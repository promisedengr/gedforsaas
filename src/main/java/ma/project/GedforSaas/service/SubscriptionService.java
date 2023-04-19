package ma.project.GedforSaas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.Subscription;
import ma.project.GedforSaas.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	public Subscription findByUserId(Long id) {
		return subscriptionRepository.findByUserId(id);
	}

	public Subscription addSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}
	
	public List<Subscription> findAllSubscription(){
		return subscriptionRepository.findAll();
	}
	
	public Subscription updateSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}
	
//	public Subscription findSubscriptionById(Long id) {
//		return subscriptionRepository.findSubscriptionByd(id)
//				.orElseThrow(() -> new ResourceNotFoundExceptionConstimized(ID_NOT_FOUND));
//	}
	
	public void deleteSubscription(Long id) {
		subscriptionRepository.deleteSubscriptionById(id);
	}

	public List<Subscription> findByClientSecretAndReference(String clientSecret, String reference) {
		return subscriptionRepository.findByClientSecretAndReference(clientSecret, reference);
	}

	public Optional<Subscription> findById(Long id) {
		return this.subscriptionRepository.findById(id);
	}
}
