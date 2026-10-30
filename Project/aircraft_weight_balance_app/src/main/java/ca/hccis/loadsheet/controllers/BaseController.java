package ca.hccis.loadsheet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Base controller which control general functionality in the app.
 *
 * @since 20220624
 * @author BJM
 */
@Controller
public class BaseController {

    /**
     * Send the user to the welcome view
     *
     * @since 20220624
     * @author BJM
     */
    @RequestMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Send the user to the about view.
     *
     * @since 20220624
     * @author BJM
     */
    @RequestMapping("/about")
    public String about() {
        return "other/about";
    }
}
