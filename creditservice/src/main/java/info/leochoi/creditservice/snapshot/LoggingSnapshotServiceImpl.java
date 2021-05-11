package info.leochoi.creditservice.snapshot;

import info.leochoi.creditservice.rule.Rule;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingSnapshotServiceImpl implements SnapshotService {

  private static final Logger logger = LoggerFactory.getLogger(LoggingSnapshotServiceImpl.class);

  @Override
  public void snapshotRules(@NotNull List<Rule> rules) {
    logger.info("Snapshotted the rules for calculation, rules=[{}].", rules);
  }
}
