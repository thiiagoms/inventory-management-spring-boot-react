package io.thiiagoms.ims;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "io.thiiagoms.ims", importOptions = DoNotIncludeTests.class)
public class ArchitectureTest {

  @ArchTest
  static final ArchRule DOMAIN_MUST_BE_FRAMEWORK_INDEPENDENT =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "org.springframework..",
              "jakarta..",
              "..application..",
              "..infrastructure..",
              "..presentation..");

  @ArchTest
  static final ArchRule APPLICATION_MUST_NOT_DEPEND_ON_OUTER_LAYERS =
      noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure..", "..presentation..");

  @ArchTest
  static final ArchRule INFRASTRUCTURE_MUST_NOT_DEPEND_ON_PRESENTATION =
      noClasses()
          .that()
          .resideInAPackage("..infrastructure..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..presentation..");

  @ArchTest
  static final ArchRule PRESENTATION_MUST_NOT_DEPEND_ON_INFRASTRUCTURE =
      noClasses()
          .that()
          .resideInAPackage("..presentation..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..infrastructure..");
}
