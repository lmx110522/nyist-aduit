package com.nyist.repository;

import com.nyist.pojo.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/7/11/011.
 */
public interface ModuleRepository extends JpaRepository<Module,String> {
    Module findModuleById(String id);
}
