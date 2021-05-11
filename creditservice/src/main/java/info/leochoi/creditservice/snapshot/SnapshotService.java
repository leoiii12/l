package info.leochoi.creditservice.snapshot;

import info.leochoi.creditservice.rule.Rule;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface SnapshotService {
  void snapshotRules(@NotNull List<Rule> rules);
}
