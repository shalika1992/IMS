package com.epic.ims.annotation.accesscontrol;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.usermgt.Page;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class AccessControlAspect {

    @Autowired
    SessionBean sessionBean;

    @Around(value = "@annotation(accessControl)")
    public Object checkAccessControl(ProceedingJoinPoint proceedingJoinPoint, AccessControl accessControl) {
        try {
//            if (sessionBean != null) {
//                Map<String, List<Page>> pageMap = sessionBean.getPageMap();
//                if (pageMap != null && !pageMap.isEmpty() && pageMap.size() > 0) {
//                    String sectionCode = accessControl.sectionCode();
//                    String pageCode = accessControl.pageCode();
//                    if (sectionCode != null && pageCode != null) {
//                        List<Page> pageList = pageMap.get(sectionCode);
//                        if (pageList != null && !pageList.isEmpty() && pageList.size() > 0) {
//                            Page page = pageList.stream().filter(p -> p.getPageCode().equals(pageCode)).findFirst().orElse(null);
//                            if (page != null) {
//                                return proceedingJoinPoint.proceed();
//                            } else {
//                                return new ModelAndView("redirect:/logout.htm?error=6");
//                            }
//                        } else {
//                            return new ModelAndView("redirect:/logout.htm?error=6");
//                        }
//                    } else {
//                        return new ModelAndView("redirect:/logout.htm?error=6");
//                    }
//                } else {
//                    return new ModelAndView("redirect:/logout.htm?error=6");
//                }
//            } else {
//                return new ModelAndView("redirect:/logout.htm?error=6");
//            }

            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            return new ModelAndView("redirect:/logout.htm?error=6");
        } catch (Throwable throwable) {
            return new ModelAndView("redirect:/logout.htm?error=6");
        }
    }
}
