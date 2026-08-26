BACKEND_DIR := backend
MVNW := ./mvnw

.PHONY: help test format format-check checkstyle spotbugs architecture quality verify

help:
	@echo "Available quality commands:"
	@echo "  make test          Run backend tests and generate the JaCoCo coverage report"
	@echo "  make format        Apply automatic Java formatting"
	@echo "  make format-check  Verify Java formatting"
	@echo "  make checkstyle    Verify coding standards"
	@echo "  make spotbugs      Run potential-bug analysis"
	@echo "  make architecture  Run Clean Architecture and DDD dependency tests"
	@echo "  make quality       Run every quality gate without the full test suite"
	@echo "  make verify        Run tests and every Maven quality gate"

test:
	cd $(BACKEND_DIR) && $(MVNW) clean test

format:
	cd $(BACKEND_DIR) && $(MVNW) spotless:apply

format-check:
	cd $(BACKEND_DIR) && $(MVNW) spotless:check

checkstyle:
	cd $(BACKEND_DIR) && $(MVNW) checkstyle:check

spotbugs:
	cd $(BACKEND_DIR) && $(MVNW) -DskipTests compile spotbugs:check

architecture:
	cd $(BACKEND_DIR) && $(MVNW) -Djacoco.skip=true -Dtest='io.thiiagoms.ims.ArchitectureTest' test

quality:
	@$(MAKE) --keep-going format-check checkstyle spotbugs architecture

verify:
	cd $(BACKEND_DIR) && $(MVNW) verify
