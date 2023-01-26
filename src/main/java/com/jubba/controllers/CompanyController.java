package com.jubba.controllers;

import com.jubba.emails.ScheduleEmail;
import com.jubba.models.Company;
import com.jubba.models.Schedule;
import com.jubba.payload.request.NoticeRequest;
import com.jubba.payload.request.ScheduleRequest;
import com.jubba.payload.response.MessageResponse;
import com.jubba.payload.response.ScheduleResponse;
import com.jubba.repository.CompanyRepository;
import com.jubba.repository.ScheduleRepository;
import com.jubba.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/companys")
public class CompanyController {
  @Autowired
  CompanyRepository companyRepository;

  @Autowired
  ScheduleRepository scheduleRepository;

  @GetMapping("/all")
  public ResponseEntity<?> companyAll(){
    List<Company> companys = companyRepository.findAll();
    return ResponseEntity.ok().body(companys);
  }
  @PostMapping("/edit-companys")
  public ResponseEntity<?> companyEdit(@Valid @RequestBody String json){
    companyRepository.findById(1).ifPresent(id1 -> {
      id1.setJson(json);
      companyRepository.save(id1);
    });

    return ResponseEntity.ok().body(new MessageResponse("Change stores with sucess"));
  }
  @PostMapping("/notice-companys")
  public ResponseEntity<?> companyNotice(@Valid @RequestBody NoticeRequest notice){
    boolean send = new SendEmail().message(notice.email, notice.assunto, new ScheduleEmail().messagem(notice.servico , notice.assunto));
    if (send) {
      return ResponseEntity.ok().body(new MessageResponse("Recovery email sent"));
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Error sending recovery email!"));
  }

  @PostMapping("/schedule-create")
  public ResponseEntity<?> scheduleCreate(@Valid @RequestBody ScheduleRequest schedule){
    Schedule scheduleNew = new Schedule(schedule.json, schedule.idCliente);
    scheduleRepository.save(scheduleNew);
    return ResponseEntity.ok().body(new MessageResponse("Servico adicionado"));
  }

  @GetMapping("/schedule-delete/{id}")
  public ResponseEntity<?> scheduleDelete(@PathVariable("id") String id){
    scheduleRepository.deleteById(Integer.valueOf(id));
    return ResponseEntity.ok().body(new MessageResponse("Servico removido"));
  }

  @GetMapping("/schedule-user/{id}")
  public ResponseEntity<?> scheduleUser(@PathVariable("id") String id){
    List<Schedule> schedules_user = scheduleRepository.findByIdCliente(Integer.valueOf(id));

    return ResponseEntity.ok().body(schedules_user);
  }

}