package customers;

import customers.domain.Address;
import customers.domain.Student;
import customers.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// for students

		System.out.println("-----------Students ----------------");

		// if existed delete all
		studentRepository.deleteAll();

		Student s1 = new Student("Ava","213-555-7821","ava.miller@gmail.com",
				new Address("Sunset Blvd","Los Angeles","California"));
		studentRepository.save(s1);

		Student s2 = new Student("Max","305-444-1298","ethan.james@gmail.com",
				new Address("Ocean Drive","Miami","Florida"));
		studentRepository.save(s2);

		Student s3 = new Student("Sophia","212-777-3456","sophia.wilson@gmail.com",
				new Address("5th Avenue","New York","New York"));
		studentRepository.save(s3);

		Student s4 = new Student("Max","312-888-6543","mason.taylor@gmail.com",
				new Address("Lake Shore Drive","Chicago","Illinois"));
		studentRepository.save(s4);

		Student s5 = new Student("Isabella","713-999-4321","isabella.moore@gmail.com",
				new Address("Main Street","Houston","Texas"));
		studentRepository.save(s5);
		Student s6 = new Student("Emanuel","713-456-4761","emanuel@gmail.com",
				new Address("5Th Street","Houston","Texas"));
		studentRepository.save(s6);
		System.out.println("-----------Getting All Students ----------------");
		List<Student> students = studentRepository.findAll();
		for (Student student : students){
			System.out.println(student);
		}

		System.out.println("Getting All Student By Name : Max");
		for (Student student : studentRepository.findByName("Max")){
			System.out.println(student);
		}
		String phoneNumber = "212-777-3456";
		System.out.println("Getting A Student By Phone Number : " + phoneNumber);
		System.out.println(studentRepository.findByPhoneNumber(phoneNumber));

		String addressCity = "Houston";
		System.out.println("Getting All Student By City : "+addressCity);
		for(Student student : studentRepository.findByAddressCity(addressCity)){
			System.out.println(student);
		}

	}

}
