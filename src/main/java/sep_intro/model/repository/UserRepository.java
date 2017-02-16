package sep_intro.model.repository;

import sep_intro.model.User;

public interface UserRepository extends Repository<User, Integer> {
	User getByUserName(String username);
	void deleteByUsername(String username);
}
