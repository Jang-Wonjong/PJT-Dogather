package com.dogather.pjtserver.service;

import com.dogather.pjtserver.dao.UserDao;
import com.dogather.pjtserver.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Override
    public int userRegister(UserDto userDto) {
        int created = userDao.userRegister(userDto);
        if( created == 1){
            return 1;
        }else{
            return 0;
        }
    }

	@Override
	public UserDto userLogin(UserDto userDto) { // �α���
		UserDto user = userDao.userFind(userDto.getUserId()); // ������ ���̵� ���� ���� ����
		if(user != null){// �ִ� ���̵�
			//PW check
			if(user.getUserPw().equals(userDto.getUserPw())) {
				//�α��� ����
				return user;
			}else {
				//�α��� ����
				return userDto; // => pk : 0
			}
		}else {
			// ���� ���̵�
			return null;
		}
	}

	@Override
	public UserDto userFind(String userId) {
		return userDao.userFind(userId); // ������ ���̵� ���� �������� ����
	}
}
