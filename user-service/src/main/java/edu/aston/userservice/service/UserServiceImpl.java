package edu.aston.userservice.service;

import edu.aston.userservice.entity.User;
import edu.aston.userservice.dto.UserRequestDTO;
import edu.aston.userservice.dto.UserResponseDTO;
import edu.aston.userservice.producer.UserEventProducer;
import edu.aston.userservice.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public UserServiceImpl(final UserRepository userRepository,
                           final UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(final UserRequestDTO userRequestDTO) throws UserServiceException {
        final String name = userRequestDTO.getName();
        final String email = userRequestDTO.getEmail();
        final Integer age = userRequestDTO.getAge();

        logger.info("Start creating a new user: [name={}, email={}, age={}].", name, email, age);

        try {
            final User user = this.userRepository.save(new User(name, email, age));

            logger.info("The user has been created: {}", user.toString());

            this.userEventProducer.sendEvent("CREATE", email);

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            logger.error("Failed to add a new user to the database.");
            throw new UserServiceException("Failed to add a new user to the database", exception);
        }
    }

    @Override
    @Transactional
    public List<UserResponseDTO> findAll() {
        logger.info("Start searching for all users in the database.");

        final List<UserResponseDTO> list = this.userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());

        logger.info("Found {} users in the database.", list.size());

        return list;
    }

    @Override
    @Transactional
    public UserResponseDTO findById(final Integer id) throws UserServiceException {
        logger.info("Start searching a user by ID: [id={}].", id);

        try {
            final User user = this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            logger.info("Found a user in the database: {}.", user.toString());

            return new UserResponseDTO(user);
        }
        catch (Exception exception) {
            logger.error("Failed to find the user by ID in the database.");
            throw new UserServiceException("Failed to find the user by ID in the database", exception);
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(final Integer id, final UserRequestDTO userRequestDTO) throws UserServiceException {
        final String name = userRequestDTO.getName();
        final String email = userRequestDTO.getEmail();
        final Integer age = userRequestDTO.getAge();

        logger.info("Start updating user information: [id={}, name={}, email={}, age={}].", id, name, email, age);

        try {
            this.userRepository.findById(id)
                    .orElseThrow(() -> new UserServiceException("The user could not be found with ID: " + id));

            final User user = this.userRepository.save(new User(id, name, email, age));

            logger.info("The user with ID {} has been updated in the database.", id);

            return new UserResponseDTO(user);
        }
        catch(Exception exception) {
            logger.error("Failed to update user information in the database.");
            throw new UserServiceException("Failed to update user information in the database", exception);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(final Integer id) throws UserServiceException {
        logger.info("Deleting a user from the database: [id={}].", id);

        try {
            final Optional<User> optional = this.userRepository.findById(id);

            if(optional.isPresent()) {
                userRepository.deleteById(id);

                logger.info("The user with ID {} was deleted from the database.", id);

                this.userEventProducer.sendEvent("DELETE", optional.get().getEmail());

                return true;
            }

            return false;
        }
        catch(Exception exception) {
            logger.error("Failed to delete a user information from the database.");
            throw new UserServiceException("Failed to delete a user information from the database", exception);
        }
    }
}
