package sg.edu.iss.demo.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sg.edu.iss.demo.exception.BusinessException;

/** Runs a business action and turns the outcome into a one-time message on the next page. */
final class Flash {

	private Flash() {
	}

	static String run(RedirectAttributes ra, String redirectTo, String successMessage, Runnable action) {
		try {
			action.run();
			ra.addFlashAttribute("msg", successMessage);
		} catch (BusinessException ex) {
			ra.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:" + redirectTo;
	}
}
