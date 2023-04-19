package ma.project.GedforSaas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class GedForSaasApplicationTests {

	@Test
	void contextLoads() {
	}
	
	Calculator underTest = new Calculator();
	
	@Test
	void itShouldAddTwoNumbers() {
		//given
		int number0ne = 20 ; 
		int numberTwo = 30 ; 
		
		//when
		int result = underTest.add(number0ne, numberTwo);
		
		//then
		int expected = 50 ; 
		assertThat(result).isEqualTo(expected);
	}
	
	class Calculator {
		int add(int a , int b) {
			return a +b; 
		}
	}

}
