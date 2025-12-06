package com.example.To_Do_List.services.impl;

import com.example.To_Do_List.DTO.TaskDTO;
import com.example.To_Do_List.DTO.UserDTO;
import com.example.To_Do_List.Errors.UserNotFound;
import com.example.To_Do_List.Errors.User_already_exists;
import com.example.To_Do_List.model.User;
import com.example.To_Do_List.model.UserRoles;
import com.example.To_Do_List.repository.UserRepository;
import com.example.To_Do_List.repository.UserRoleRepository;
import com.example.To_Do_List.services.TaskServices;
import com.example.To_Do_List.services.UserServices;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;



@Slf4j
@Service

public class UserServiceImpl implements UserServices {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    public final TaskServices taskServices;
    @Autowired
    private final UserRoleRepository userRoleRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TaskServices taskServices, UserRoleRepository userRoleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskServices = taskServices;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        log.info("UserServiceIml инициализирован");
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "'all'") //кеширование результатов методов
    public List<UserDTO> findAll() {
        log.debug("Получение списка всех пользователей");
       List<User> users = userRepository.findAll();
       List<UserDTO> userDTOS = new ArrayList<>();
       for (User us: users){
           userDTOS.add(modelMapper.map(us,UserDTO.class));
       }
       return userDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        log.debug("Получение пользователя по id {}", id);
        Optional<User> user = userRepository.findById(id);
        if( user.isPresent()) return modelMapper.map(user.get(), UserDTO.class);
        else throw new UserNotFound(String.format("Пользователь с таким ID %s остуствует",String.valueOf(id)));
    }



    @Override
    @Transactional
    @CacheEvict(cacheNames = "users",allEntries = true) // очистка кеша, все записи из этого кеша удаляются
    public void createNewUser(UserDTO userdto) {
        log.debug("Добавление нового пользователя: {}",userdto.getUsername());
        User users = userRepository.findByUsernameAndSurname(userdto.getUsername(), userdto.getSurname());
        if(users == null){
            User user = new User();
            user.setUsername(userdto.getUsername());
            user.setEmail(userdto.getEmail());
            user.setSurname(userdto.getSurname());
            user.setDateOfBirthday(userdto.getDateOfBirthday());
            user.setPassword(passwordEncoder.encode("topsecret"));
            var userRole = userRoleRepository.findRoleByName(UserRoles.USER).orElseThrow(() -> new RuntimeException("Роль USER не найдена"));
            user.setRoles(List.of(userRole));;
            userRepository.save(user);
            log.info("Успешное добавление нового пользователя: {}",userdto.getUsername());
        }else throw new User_already_exists("Пользователь уже существует");

    }

    @Override
    public int increamentProductivity(Long id)  {
        log.debug("Увеличение продуктивности пользователя");
        if (userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            user.incrementProductivity();
            User userUpDate = userRepository.save(user);
            log.info("Успешное уведечение продуктивности пользователя {}",user.getUsername());
            return userUpDate.getProductivity();

        }else throw new UserNotFound(String.format("Пользователь с таким ID %s остуствует",String.valueOf(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getUserTask(Long id) throws UserNotFound {
        log.debug("Получение списка всех задач поьзователя");
        return taskServices.taskByUserId(id);
    }

    @Override
    public Page<UserDTO> allUsersPaginated(Pageable pageable) {
        log.debug("Получение пользователей с пагинацией: {}, размер {}",pageable.getPageNumber(),pageable.getPageSize());
        return userRepository.findAll( pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));


    }

    @Override
    public Page<UserDTO> searchUsers(String search, Pageable pageable) {
        log.info("Поиск пользователей: '{}', страница: {}, размер: {}",
                search, pageable.getPageNumber(), pageable.getPageSize());

        // Используем исправленный метод репозитория
        Page<User> userPage = userRepository.findByUsernameContainingIgnoreCaseOrSurnameContainingIgnoreCase(
                search, search, pageable);

        log.info("Найдено пользователей: {}", userPage.getTotalElements());

        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users", "user", "userTasks"}, allEntries = true) // удаляет все кеши
    public void deleteById(long id) throws UserNotFound {
        log.debug("Удаление пользователя с таким id {}", id);
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.deleteById(id);
            log.info("Успешное удаление пользователя с таким id {}", id);
        }else throw new UserNotFound("Невозможно удалить пользователя с таким ID " + String.valueOf(id) + " , так как пользователя нет" );

    }

}
