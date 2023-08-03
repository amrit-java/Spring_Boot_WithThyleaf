package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.PageRanges;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//methode for adding  coman data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		
		String userName = principal.getName();
		System.err.println("USERNAME"+userName);
		
		User user = userRepository.getUserByUserName(userName);
		System.err.println(user);
		
		model.addAttribute("user",user);
		
	}
	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
				
	}
	
	//open add form controller
	@GetMapping("/add-contact")
	public String openAddCotactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}
	
	//process add contact form
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			
			String name=principal.getName();
			User user= this.userRepository.getUserByUserName(name);
			
			/*
			 * if(3>2) { throw new Exception(); }
			 */
			
			//processing and uploding file
			if(file.isEmpty()) {
				//if the file is empty then try our message
				contact.setImage("contact.png");
			}else {
				//file the file to folder and update the name to contact
				contact.setImage(file.getOriginalFilename());
				File savefile=new ClassPathResource("static/img").getFile();
				
			Path path=	Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				
			
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			System.out.println("Add to data base");
			
			System.err.println(contact);
			//success msg
		session.setAttribute("message",new Message("your contact is added !! Add more..","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("ERROR"+e.getMessage());
			//error msg
			session.setAttribute("message",new Message("some went wrong !! try again..","danger"));
		}
		
		
		
		return "normal/add_contact_form";
	}
	
	//show contact view handler
	//per page = 5[n]
	//current page = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page, Model model,Principal principal) {
		//send title
		model.addAttribute("title","show User Contact");
		//contact ki list ko bhejni h
		
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		
		PageRequest pageable = PageRequest.of(page, 5);
		Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_contacts";
		
	}
	
	//showing particular detail
	@RequestMapping("/{cId}/contact")
	public String showContactDetail( @PathVariable("cId") Integer cId,Model model,Principal principal) {
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact=contactOptional.get();
		String userName=principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		if(user.getId()==contact.getUser().getId())
		
		model.addAttribute( "contact" ,contact);
	
		return "normal/contact_detail";
	}
	
	//delete contact handeral
	@GetMapping("/delete/{cId}")
	public String deletecontact(@PathVariable("cId")Integer cId,Model model,HttpSession session,Principal principal) {
		
		Contact contact = this.contactRepository.findById(cId).get();
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		this.userRepository.save(user);
		
		session.setAttribute("message",new Message("contact delete successfully...", "success"));
		
		
		return "redirect:/user/show-contacts/0";
		
	}
	
	//open UpDate form update
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId")Integer cId,Model model) {
		model.addAttribute("title","update Contact");
		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute( "contact" ,contact);
		return "normal/update_form";
	}
	
	//update contact handler
	@RequestMapping(value= "/process-update", method= RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,Principal principal) {
		try {
			//old contact details
			Contact oldcontact = this.contactRepository.findById(contact.getCId()).get();
			//image
			if(!file.isEmpty()) {
				//delete old photo
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldcontact.getImage());
				file1.delete();
				
				//update new photo
				File savefile=new ClassPathResource("static/img").getFile();
				
				Path path=	Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
					contact.setImage(file.getOriginalFilename());
				
			}else {
				contact.setImage(oldcontact.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message",new Message("Your contact is updated..", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getCId()+"/contact";
	}
	
	//Your profile handler
	@GetMapping("/profile")
	public String yourProfile( Model model) {
		
		model.addAttribute("titlr","Profile Page");
		
		return "normal/profile";
		
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session) {
		
		System.out.println("oldPassword"+oldPassword);
		System.out.println("newPassword"+newPassword);
		String userName =principal.getName();
		User currentUser= this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			//change the password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message",new Message("change password successfully...", "success"));
		}else {
			//error...
			session.setAttribute("message",new Message("Please Enter correct old password...", "danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}

}
