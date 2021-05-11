package info.leochoi.creditservice.scoring.service;

import info.leochoi.creditservice.scoring.ScoringInput;
import org.jetbrains.annotations.NotNull;

public interface ScoringService {

  long calculateScore(@NotNull ScoringInput scoringInput);
}
