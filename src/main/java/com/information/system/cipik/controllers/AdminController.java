package com.information.system.cipik.controllers;

import com.ibm.icu.text.Transliterator;
import com.information.system.cipik.exception.NotFoundException;
import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.services.AdminService;
import com.information.system.cipik.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class AdminController {

    @Autowired
    CentrRepository centrRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    OtdelRepository otdelRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AdminService adminService;

    @Autowired
    private HistoryAuthorizationRepository historyAuthorizationRepository;

    /**
     * Установка логов для действий пользователя
     *
     * @param username - имя пользователя
     * @return отформатированная строка
     */
    private String setUserLog(String username) {
        return "----" + " User: " + username + " --> ";
    }

//////////////пользователи created by Tashmetov Tahir////////////////////////

    @GetMapping("/admin/all-users")
    public String userList(Model model, Authentication authentication) {
        //Получаем пользователя, под которым выполнен вход (страница доступна только апдмину, соответсвенно пользователь будет только с ролью админа.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Получаем списко всех пользователей, для отображения на странице
        List<User> var_list = userService.allUsers();
        //Удаляем текущего пользователя из списка для отображения, чтобы он не мог удалить сам себя
        var_list.remove(userRepository.findByUsername(auth.getName()));
        Iterable<User> users = var_list;
        model.addAttribute("allUsers", users);
        log.warn(setUserLog(authentication.getName()) + "Open admin page with users");
        return "admin/all-users";
    }

    /**
     * Формируем динамически страницу для каждого пользователя. Внутри страницы можно сделать операции над пользователем
     */
    @GetMapping("/admin/all-users/user-details/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/admin/all-users";
        }
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> res = new ArrayList<>();
        user.ifPresent(res::add);
        model.addAttribute("user", res);
        Iterable<Role> roles = roleRepository.findAll();
        //удаление из списка ролей тех что уже назначены пользователю
        Iterator<Role> itr = roles.iterator();
        List<Role> userRoles = new ArrayList<>(res.get(0).getRoles());
        //цикл удаления
        while (itr.hasNext()) {
            Role role = itr.next();
            for (Role r : userRoles) {
                if (r.equals(role)) {
                    itr.remove();
                }
            }
        }
        model.addAttribute("roles", roles);
        //Создаем объект var_AdminRole и добавляем его в атрибуты страницы.
        //чтобы потом определять пользователей у которого есть такая роль и не выводить кнопку "Сделать администратором" на странице user-details
        Role var_AdminRole = new Role(2L, "ROLE_ADMIN");
        model.addAttribute("var_AdminRole", var_AdminRole);
        return "admin/user-details";
    }

    @PostMapping("/admin/all-users/user-details/{id}/add_admin")
    public String add_admin(@PathVariable(value = "id") long id, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        log.warn(setUserLog(authentication.getName()) + "ADD role ADMIN to: {}", user.getUsername());
        userRepository.save(user);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/all-users/user-details/{id}/remove")
    public String delete_user(@PathVariable(value = "id") long id, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        log.warn(setUserLog(authentication.getName()) + "DELETE user: {}", user.getUsername());
        userRepository.delete(user);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/admin/all-users/add-user")
    public String add_user(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "admin/add-user";
    }

    @PostMapping("/admin/all-users/add-user")
    public String save_user(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam Long dropRole, Authentication authentication) {
        log.warn(setUserLog(authentication.getName()) + "CREATE new user: {}", username);
        Role role = roleRepository.findById(dropRole).orElseThrow(() -> new NotFoundException("Role " + dropRole + " not found"));
        User user = new User(username, password, password);
        user.setRoles(Collections.singleton(role));
        log.warn(setUserLog(authentication.getName()) + "SET list Roles to user" + username + ": {}", user.getRoles());
        userService.saveUser(user);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/all-users/user-details/{user_id}/add_roles/{selRecs}")
    public String addRolesToUser(@PathVariable(value = "user_id") long id,
                                 @PathVariable(value = "selRecs") List<Long> ids,
                                 Model model, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        Set<Role> roles = new HashSet<>();
        for (Long i : ids) {
            roles.add(roleRepository.findById(i).orElseThrow(() -> new NotFoundException("Role with ID " + i + " not found")));
        }
        roles.addAll(user.getRoles());
        user.setRoles(roles);
        userRepository.save(user);
        log.warn(setUserLog(authentication.getName()) + "SET list Roles to user" + user.getUsername() + ": {}", user.getRoles());
        ArrayList<User> res = new ArrayList<>();
        res.add(user);
        model.addAttribute("user", res);
        return "admin/user-details :: user-info";
    }

    @PostMapping("/admin/all-users/user-details/{user_id}/remove-role/{role_id}")
    public String deleteRoleFromUser(@PathVariable(value = "user_id") long u_id,
                                     @PathVariable(value = "role_id") long r_id,
                                     Model model, Authentication authentication) {
        User user = userRepository.findById(u_id).orElseThrow(() -> new NotFoundException("User with ID " + u_id + " not found"));
        Role role = roleRepository.findById(r_id).orElseThrow(() -> new NotFoundException("Role with ID " + r_id + " not found"));
        user.getRoles().remove(role);
        userRepository.save(user);
        log.warn(setUserLog(authentication.getName()) + "DELETE Role from user " + user.getUsername() + ": {}", role.getName());
        ArrayList<User> res = new ArrayList<>();
        res.add(user);
        model.addAttribute("user", res);
        return "admin/user-details :: user-info";
    }

    ////////////////центра///////////////
    @GetMapping("/admin/centrs/add")
    public String centrAll(Model model) {
        Iterable<Centr> centrs = centrRepository.findAll();
        model.addAttribute("centrs", centrs);
        return "admin/admin-centr";
    }

    @PostMapping("/admin/centrs/add")
    public String centrAdd(@RequestParam String name,
                           @RequestParam String shortName, Authentication authentication) {
        Centr cent = new Centr(name, shortName);
        centrRepository.save(cent);
        log.warn(setUserLog(authentication.getName()) + "ADD new Centr: {}", cent);
        return "redirect:/admin/centrs/add";
    }

    @GetMapping("/admin/centrs/{id}/edit")
    public String centrEdit(@PathVariable(value = "id") long id, Model model) {
        if (!centrRepository.existsById(id)) {
            return "redirect:/admin/centrs/add";
        }
        Centr centr = centrRepository.findById(id).orElseThrow(() -> new NotFoundException("Centr with ID " + id + " not found"));
        model.addAttribute("centr", centr);
        return "admin/admin-centr-edit";
    }

    @PostMapping("/admin/centrs/{id}/edit")
    public String centrUpdate(@PathVariable(value = "id") long id,
                              @RequestParam String name,
                              @RequestParam String shortName, Authentication authentication) {
        Centr centr = centrRepository.findById(id).orElseThrow(() -> new NotFoundException("Centr with ID " + id + " not found"));
        log.warn(setUserLog(authentication.getName()) + "EDIT Centr: {}", centr);
        centr.setName(name);
        centr.setShortName(shortName);
        log.warn(setUserLog(authentication.getName()) + "Centr after editing: {}", centr);
        centrRepository.save(centr);
        return "redirect:/admin/centrs/add";
    }

    @PostMapping("/admin/centrs/{id}/remove")
    public String centrDelete(@PathVariable(value = "id") long id, Authentication authentication) {
        if (centrRepository.existsById(id)) {
            log.warn(setUserLog(authentication.getName()) + "DELETE Centr with ID: {}", id);
            centrRepository.deleteById(id);
        }
        return "redirect:/admin/centrs/add";
    }

    ////////////////////комплексы////////////////////////////////
    @GetMapping("/admin/komplexs/add")
    public String komplexAll(Model model) {
        Iterable<Centr> centrs = centrRepository.findAll();
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("centrs", centrs);
        model.addAttribute("komplexs", komplexs);
        return "admin/admin-komplex";
    }

    @PostMapping("/admin/komplexs/add")
    public String komplexAdd(@RequestParam String name,
                             @RequestParam String shortName,
                             @RequestParam String adres,
                             @RequestParam Long dropCentr, Authentication authentication) {
        Centr centr = centrRepository.findById(dropCentr).orElseThrow(() -> new NotFoundException("Centr with ID " + dropCentr + " not found"));
        Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");
        Role role = new Role(toLatinTrans.transliterate("ROLE_" + shortName));
        roleRepository.save(role);
        Komplex komplex = new Komplex(name, adres, shortName, centr, role);
        log.warn(setUserLog(authentication.getName()) + "ADD new Komplex: {}", komplex);
        komplexRepository.save(komplex);
        return "redirect:/admin/komplexs/add";
    }

    @GetMapping("/admin/komplexs/{id}/edit")
    public String komplexEdit(@PathVariable(value = "id") long id, Model model) {
        if (!komplexRepository.existsById(id)) {
            return "redirect:/admin/komplexs/add";
        }
        Iterable<Centr> centrs = centrRepository.findAll();
        Komplex komplex = komplexRepository.findById(id).orElseThrow(() -> new NotFoundException("Komplex with ID " + id + " not found"));
        model.addAttribute("centrs", centrs);
        model.addAttribute("komplex", komplex);
        return "admin/admin-komplex-edit";
    }

    @PostMapping("/admin/komplexs/{id}/edit")
    public String komplexUpdate(@PathVariable(value = "id") long id,
                                @RequestParam String name,
                                @RequestParam String shortName,
                                @RequestParam String adres,
                                @RequestParam Centr centr, Authentication authentication) {
        Komplex komplex = komplexRepository.findById(id).orElseThrow(() -> new NotFoundException("Komplex with ID " + id + " not found"));
        log.warn(setUserLog(authentication.getName()) + "Komplex before Editing: {}", komplex);
        if (komplex.getRole() == null) {
            Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");
            Role role = new Role(toLatinTrans.transliterate("ROLE_" + shortName));
            roleRepository.save(role);
            komplex.setRole(role);
        }

        komplex.setName(name);
        komplex.setShortName(shortName);
        komplex.setAdres(adres);
        komplex.setCentr(centr);
        log.warn(setUserLog(authentication.getName()) + "Komplex after Editing: {}", komplex);
        komplexRepository.save(komplex);
        return "redirect:/admin/komplexs/add";
    }

    @PostMapping("/admin/komplexs/{id}/remove")
    public String komplexDelete(@PathVariable(value = "id") long id, Authentication authentication) {
        if (komplexRepository.existsById(id)) {
            log.warn(setUserLog(authentication.getName()) + "DELETE Komplex with ID: {}", id);
            komplexRepository.deleteById(id);
        }
        return "redirect:/admin/komplexs/add";
    }
    ///////////////////отделы/////////////////////////////не задействованы в реальном использовании

    @GetMapping("/admin/otdels/add")
    public String otdelAll(Model model) {
        Iterable<Otdel> otdels = otdelRepository.findAll();
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("otdels", otdels);
        model.addAttribute("komplexs", komplexs);
        return "admin/admin-otdel";
    }

    @PostMapping("/admin/otdels/add")
    public String otdelAdd(@RequestParam String name,
                           @RequestParam Long dropKomplex) {
        Komplex komplex = komplexRepository.findById(dropKomplex).orElse(null);
        Otdel otdel = new Otdel(name, komplex);
        otdelRepository.save(otdel);
        return "redirect:/admin/otdels/add";
    }

    @GetMapping("/admin/otdels/{id}/edit")
    public String otdelEdit(@PathVariable(value = "id") long id, Model model) {
        if (!otdelRepository.existsById(id)) {
            return "redirect:/admin/otdels/add";
        }
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        Otdel otdel = otdelRepository.findById(id).orElse(null);
        model.addAttribute("komplexs", komplexs);
        model.addAttribute("otdel", otdel);
        return "admin/admin-otdel-edit";
    }

    @PostMapping("/admin/otdels/{id}/edit")
    public String otdelUpdate(@PathVariable(value = "id") long id,
                              @RequestParam String name,
                              @RequestParam Komplex komplex) {
        Otdel otdel = otdelRepository.findById(id).orElse(null);
        otdel.setName(name);
        otdel.setKomplex(komplex);
        otdelRepository.save(otdel);
        return "redirect:/admin/otdels/add";
    }

    @PostMapping("/admin/otdels/{id}/remove")
    public String otdelDelete(@PathVariable(value = "id") long id) {
        Otdel otdel = otdelRepository.findById(id).orElse(null);
        otdelRepository.deleteById(id);
        return "redirect:/admin/otdels/add";
    }

    /////////////////settings/////////////////////

    @PostMapping("/admin/settings/save-db-data")
    public String saveDBSettings(@RequestParam String user_name,
                                 @RequestParam String pass,
                                 @RequestParam String db_name, Authentication authentication) {
        if (user_name.equals("") || pass.equals("") || db_name.equals("")) {
            log.warn(setUserLog(authentication.getName()) + "Database authentication data must not be empty!");
        } else {
            adminService.saveDBSettings(user_name, pass, db_name);
            log.warn(setUserLog(authentication.getName()) + "SAVE New DB Settings: {}", user_name, db_name);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/settings/save-path-data")
    public String savePathsSettings(@RequestParam String mysqlPath,
                                    @RequestParam String backupPath, Authentication authentication) {
        log.warn(setUserLog(authentication.getName()) + "SAVE Paths DB (mySQLPath, backupPath): {}", mysqlPath, backupPath);
        adminService.savePathsSettings(mysqlPath, backupPath);
        return "redirect:/admin";
    }

    ///////////////authorization history//////////////////

    @GetMapping("/admin/auth-history")
    public String authHistory(Model model, Authentication authentication) {
        log.warn(setUserLog(authentication.getName()) + "Open history authorization page");
        Iterable<HistoryAuthorization> histories = historyAuthorizationRepository.findAllByOrderByAuthorizationTimeDesc();
        model.addAttribute("histories", histories);
        return "admin/authorization-history";
    }

    @GetMapping("/admin/auth-history/{t1}/{t2}/{str}")
    public String filterHistory(Model model,
                                @PathVariable("t1") String t1,
                                @PathVariable("t2") String t2,
                                @PathVariable("str") String str) throws ParseException {

        t1 = t1 + " 00:00:00";
        t2 = t2 + " 23:59:59";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = format.parse(t1);
        Date date2 = format.parse(t2);
        List<HistoryAuthorization> ha;
        if (str.equals("0")) {
            ha = historyAuthorizationRepository.findAllBySelectedDateOrderByAuthorizationTimeDesc(date1, date2);
        } else {
            ha = historyAuthorizationRepository.findAllBySelectedDateAndUsernameOrderByAuthorizationTimeDesc(date1, date2, str);
        }
        model.addAttribute("histories", ha);
        return "admin/authorization-history :: table-ha";
    }

}
