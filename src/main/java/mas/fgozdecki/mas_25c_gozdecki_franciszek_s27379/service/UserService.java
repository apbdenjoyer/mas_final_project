package mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.service;

import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.model.Account;
import mas.fgozdecki.mas_25c_gozdecki_franciszek_s27379.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
