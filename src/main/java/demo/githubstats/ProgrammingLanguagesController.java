package demo.githubstats;

import DIByRik.annotations.Controller;
import DIByRik.annotations.InputMapping;
import demo.githubstats.domain.ProgrammingLanguageRanking;

import java.util.List;

@Controller
public class ProgrammingLanguagesController {
	private final ProgrammingLanguagesService programmingLanguagesService;

	public ProgrammingLanguagesController(ProgrammingLanguagesService programmingLanguagesService) {
		this.programmingLanguagesService = programmingLanguagesService;
	}

	@InputMapping(input = "getLanguageAtRank")
	public ProgrammingLanguageRanking getByPosition(int pos) {
		var language = programmingLanguagesService.getByPosition(pos);
		if (language == null) {
			throw new IllegalArgumentException("No language found at position " + pos);
		}
		return language;
	}

	@InputMapping(input = "getAllLanguages")
	public List<ProgrammingLanguageRanking> getAll() {
		return programmingLanguagesService.getAll();
	}
}
