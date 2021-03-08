package com.picpay.banking.pixkey.repository;

import com.picpay.banking.pixkey.entity.PixKeyEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 08/03/21
 */
@Repository
public interface PixKeyEventRepository extends CrudRepository<PixKeyEvent, Long> {
}
