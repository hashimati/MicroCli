package io.hashimati.microcli.commands;

import groovy.lang.Tuple2;
import io.hashimati.microcli.constants.ProjectConstants;
import io.hashimati.microcli.domains.ConfigurationInfo;
import io.hashimati.microcli.domains.Entity;
import io.hashimati.microcli.domains.EntityRelation;
import io.hashimati.microcli.services.LiquibaseGenerator;
import io.hashimati.microcli.services.MicronautEntityGenerator;
import io.hashimati.microcli.utils.GeneratorUtils;
import io.hashimati.microcli.utils.PromptGui;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine.Command;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static io.hashimati.microcli.constants.ProjectConstants.LanguagesConstants.*;
import static io.hashimati.microcli.constants.ProjectConstants.PathsTemplate.ENTITY_PATH;
import static io.hashimati.microcli.domains.EntityRelationType.OneToMany;
import static io.hashimati.microcli.domains.EntityRelationType.OneToOne;
import static io.hashimati.microcli.utils.PromptGui.*;


@Command(name = "create-relation", aliases = {"relation"}, description = "To create a new entity")
public class CreateRelationCommand implements Callable<Integer> {

    ConfigurationInfo configurationInfo;

    @Inject
    private MicronautEntityGenerator micronautEntityGenerator;

    @Inject
    private LiquibaseGenerator liquibaseGenerator;


    @Override
    public Integer call() throws Exception {
        AnsiConsole.systemInstall();

        configurationInfo = ConfigurationInfo.fromFile(new File(ConfigurationInfo.getConfigurationFileName()));
        //Load Configuration;
        ConfigurationInfo configurationInfo = ConfigurationInfo.fromFile(new File(ConfigurationInfo.getConfigurationFileName())) ;
        List<String> entities = configurationInfo
                .getEntities()
                .stream()
                .map(x->x.getName())
                .collect(Collectors.toList());


        if(entities.isEmpty()) {

            printlnWarning("Please, define entities first.");
            setToDefault();
            return 0;

        }
        EntityRelation entityRelation = new EntityRelation();
        String e1Input = PromptGui.createListPrompt("e1",
                "Select the first entity (E1):",
                entities.toArray(new String[entities.size()])).getSelectedId();

        Entity e1 = configurationInfo.getEntities().stream().filter(x->x.getName().equals(e1Input)).findFirst().get();
        entityRelation.setE1(e1.getName());
        entityRelation.setE1Package(e1.getEntityPackage());
        entityRelation.setE1Table(e1.getCollectionName());




        String e2Input = PromptGui.createListPrompt("e2",
                "Select the second entity (E2):",
                entities.toArray(new String[entities.size()])).getSelectedId();

        Entity e2 = configurationInfo.getEntities().stream().filter(x->x.getName().equals(e2Input)).findFirst().get();
        entityRelation.setE2(e2.getName());
        entityRelation.setE2Package(e2.getEntityPackage());
        entityRelation.setE2Table(e2.getCollectionName());

        String relation = PromptGui.createListPrompt("relation",
                "Select the relationship type:",
                "ONE_TO_ONE",
                    "ONE_TO_MANY").getSelectedId();

        switch (relation){
            case "ONE_TO_ONE":
                entityRelation.setRelationType(OneToOne);
                break;
            case "ONE_TO_MANY":
                entityRelation.setRelationType(OneToMany);
                break;
        }

        ArrayList relations = new ArrayList(){{
            add(entityRelation);
        }};
        String lang =  configurationInfo.getProjectInfo().getSourceLanguage().toLowerCase();
        String entityFileContent  =micronautEntityGenerator.generateEntity(e1, relations,lang);

        String extension =".";
        switch (configurationInfo.getProjectInfo().getSourceLanguage().toLowerCase())
        {
            case KOTLIN_LANG:
                extension += ProjectConstants.Extensions.KOTLIN;
                break;
            case GROOVY_LANG:
                extension += ProjectConstants.Extensions.GROOVY;
                break;
            case JAVA_LANG:
            default:
                extension += ProjectConstants.Extensions.JAVA;
                break;

        }


        String entityPath = GeneratorUtils.generateFromTemplate(ENTITY_PATH, new HashMap<String, String>(){{
            put("lang", configurationInfo.getProjectInfo().getSourceLanguage());
            put("defaultPackage", GeneratorUtils.packageToPath(configurationInfo.getProjectInfo().getDefaultPackage()));
        }});
        GeneratorUtils.createFile(System.getProperty("user.dir")+entityPath+ "/"+e1.getName()+extension, entityFileContent);




        String entity2FileContent  =micronautEntityGenerator.generateEntity(e2,relations,lang);


        String entity2Path = GeneratorUtils.generateFromTemplate(ENTITY_PATH, new HashMap<String, String>(){{
            put("lang", configurationInfo.getProjectInfo().getSourceLanguage());
            put("defaultPackage", GeneratorUtils.packageToPath(configurationInfo.getProjectInfo().getDefaultPackage()));
        }});
        GeneratorUtils.createFile(System.getProperty("user.dir")+entity2Path+ "/"+e2.getName()+extension, entity2FileContent);



        if(configurationInfo.getDataMigrationTool() != null)
        {
            if(configurationInfo.getDataMigrationTool().equalsIgnoreCase("liquibase"))
            {
                configurationInfo.setLiquibaseSequence(1 + configurationInfo.getLiquibaseSequence());
                e1.setLiquibaseSequence(configurationInfo.getLiquibaseSequence());
                e2.setLiquibaseSequence(configurationInfo.getLiquibaseSequence());

                Tuple2< String, String> content = liquibaseGenerator.generateForeignKey(e1, e2, entityRelation, configurationInfo.getLiquibaseSequence());
                GeneratorUtils.createFile(content.getV1(), content.getV2());
            }
        }
        configurationInfo.getRelations().add(entityRelation);
        configurationInfo.writeToFile();
        printlnSuccess("The relationship has been created successfully!");
        setToDefault();
        System.gc();
        return 0;
    }
}
