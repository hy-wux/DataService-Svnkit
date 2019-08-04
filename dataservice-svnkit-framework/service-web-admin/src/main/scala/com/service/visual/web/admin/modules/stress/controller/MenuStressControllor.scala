package com.service.visual.web.admin.modules.stress.controller

import java.util

import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.service.visual.web.admin.modules.upms.entity.Menu
import com.service.visual.web.admin.modules.upms.service.IMenuService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, RestController}

@RestController
@RequestMapping(Array("/stress/menu"))
class MenuStressControllor {
  @Autowired
  private val menuService: IMenuService = null

  @RequestMapping(value = Array("/list"))
  def list(@RequestParam(required = false) menuName: String): util.List[Menu] = {
    menuService.selectList(new EntityWrapper[Menu]().like("menu_name", menuName))
  }
}
