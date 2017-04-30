package cn.itcast.bos.service.qp;

import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.user.User;

public interface NoticeBillService {

	public void save(String province, String city, String district, User existUser, NoticeBill model);

}
