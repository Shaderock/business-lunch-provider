package com.shaderock.lunch.backend.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import java.io.PrintStream;

@AnalyzeClasses(packages = "com.shaderock.lunch", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTests {

  @ArchTest
  static final ArchRule layersAccessRule = Architectures.layeredArchitecture()
      .consideringOnlyDependenciesInLayers()
      .layer("Controller").definedBy("..controller..")
      .layer("Service").definedBy("..service..")
      .layer("Repository").definedBy("..repository..")
      .layer("Entities").definedBy("..entity..")

      .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
      .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
      .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
      .whereLayer("Entities").mayOnlyBeAccessedByLayers("Repository", "Service");

  @ArchTest
  static final ArchRule fieldInjectionNotAllowed = ArchRuleDefinition.noFields().should(
      ArchConditions.beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
          .as("Consider using something except direct field injection"));

  @ArchTest
  static final ArchRule systemOutNotAllowedAnywhere = ArchRuleDefinition.noClasses().should()
      .accessClassesThat().belongToAnyOf(PrintStream.class)
      .as("Consider using a LOGGER to print something");

}
