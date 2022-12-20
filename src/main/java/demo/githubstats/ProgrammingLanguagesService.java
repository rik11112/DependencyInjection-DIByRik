package demo.githubstats;

import DIByRik.annotations.Service;
import demo.githubstats.domain.ProgrammingLanguageRanking;

import java.util.List;

@Service
public class ProgrammingLanguagesService {
	private final ProgrammingLanguagesRepository programmingLanguagesRepository;

	public ProgrammingLanguagesService(ProgrammingLanguagesRepository programmingLanguagesRepository) {
		this.programmingLanguagesRepository = programmingLanguagesRepository;
	}

	public ProgrammingLanguageRanking getByPosition(int pos) {
		return programmingLanguagesRepository.getByPosition(pos);
	}

	public List<ProgrammingLanguageRanking> getAll() {
		return programmingLanguagesRepository.getAll();
	}
}
