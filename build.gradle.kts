import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer

plugins {
    id("java-library")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.27"
}

version = "${property("minecraft_version")}-${property("mod_version")}"
group = "epicsquid.roots"
//archivesBaseName = "Roots"

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
        vendor.set(org.gradle.jvm.toolchain.JvmVendorSpec.AZUL)
    }
    // Generate sources and javadocs jars when building and publishing
    withSourcesJar()
    withJavadocJar()
}

minecraft {
    mcVersion.set("1.12.2")
    
    // Username for client run configurations
    username.set("Developer")
    
//    // Generate a field named VERSION with the mod version in the injected Tags class
//    injectedTags.put("VERSION", project.version)
    
    // If you need the old replaceIn mechanism, prefer the injectTags task because it doesn't inject a javac plugin.
    // tagReplacementFiles.add("RfgExampleMod.java")
    
    // Enable assertions in the mod's package when running the client or server
    extraRunJvmArguments.add("-ea:${project.group}")
    
    // If needed, add extra tweaker classes like for mixins.
    // extraTweakClasses.add("org.spongepowered.asm.launch.MixinTweaker")
    
    // Exclude some Maven dependency groups from being automatically included in the reobfuscated runs
    groupsToExcludeFromAutoReobfMapping.addAll("com.diffplug", "com.diffplug.durian", "net.industrial-craft")
}

repositories {
    mavenLocal()
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com/")
    }
    maven {
        name = "Patchouli"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        // location of the maven that hosts JEI files
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        // location of a maven mirror for JEI files, as a fallback
        name = "ModMaven"
        url = uri("https://modmaven.k-4u.nl/")
    }
    maven {
        name = "MMD Maven"
        url= uri("https://maven.mcmoddev.com/")
    }
    maven {
        //Also contains Patchouli
        name = "Jared Maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "Tterrag Maven"
        url = uri("https://maven.tterrag.com/")
    }
}



dependencies {
    implementation(rfg.deobf("curse.maven:MysticalWorld-282940:3460961"))
    implementation(rfg.deobf("curse.maven:MysticalLib-277064:3483816"))
    compileOnly(rfg.deobf("curse.maven:JustEnoughResources-240630:2728585"))
    
    compileOnly(rfg.deobf("curse.maven:JustEnoughResources-240630:2728585") as String) {
        exclude("mezz.jei")
    }
    
    implementation(rfg.deobf("curse.maven:SimpleHarvest-240783:2897811"))
    
    implementation("vazkii.patchouli:Patchouli:${property("patchouli_version")}")
    compileOnly(rfg.deobf("curse.maven:Baubles-227083:2518667"))
    
    compileOnly(rfg.deobf("mezz.jei:jei_${property("minecraft_version")}:${property("jei_version")}"))
    
    compileOnly(rfg.deobf("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-${property("ct_version")}"))
    compileOnly(rfg.deobf("CraftTweaker2:CraftTweaker2-API:4.+"))
    compileOnly("CraftTweaker2:ZenScript:4.0.+")
    
    
    compileOnly(rfg.deobf("vazkii.botania:Botania:${property("botania_version")}") as String){
        exclude("mezz.jei")
    }
    
    compileOnly(rfg.deobf("curse.maven:Thaumcraft6-223628:2629023"))
    
    compileOnly(rfg.deobf("curse.maven:Hwyla-253449:2568751") as String) {
        exclude("mezz.jei")
    }
    
    compileOnly(rfg.deobf("mcjty.theoneprobe:TheOneProbe-${property("top_version")}"))
}


tasks.processResources.configure {
    val projVersion = project.version.toString() // Needed for configuration cache to work
    val mcversion = project.property("minecraft_version").toString()
    inputs.property("version", projVersion)
    inputs.property("mcversion", mcversion)
    
    filesMatching("mcmod.info") {
        expand(mapOf("version" to projVersion, "mcversion" to mcversion))
    }
}

//// Generates a class named rfg.examplemod.Tags with the mod version in it, you can find it at
//tasks.injectTags.configure {
//    outputClassName.set("${project.group}.Tags")
//}

tasks.javadoc.configure {
    (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
}


// Add an access tranformer
tasks.deobfuscateMergedJarToSrg.configure {accessTransformerFiles.from("src/main/resources/META-INF/roots_at.cfg")}


idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        inheritOutputDirs = true // Fix resources in IJ-Native runs
    }
    project {
        this.withGroovyBuilder {
            "settings" {
                "runConfigurations" {
                    val self = this.delegate as RunConfigurationContainer
                    self.add(Gradle("1. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    })
                    self.add(Gradle("2. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    })
                    self.add(Gradle("3. Run Obfuscated Client").apply {
                        setProperty("taskNames", listOf("runObfClient"))
                    })
                    self.add(Gradle("4. Run Obfuscated Server").apply {
                        setProperty("taskNames", listOf("runObfServer"))
                    })
                    /*
                    These require extra configuration in IntelliJ, so are not enabled by default
                    self.add(Application("Run Client (IJ Native, Deprecated)", project).apply {
                      mainClass = "GradleStart"
                      moduleName = project.name + ".ideVirtualMain"
                      afterEvaluate {
                        val runClient = tasks.runClient.get()
                        workingDirectory = runClient.workingDir.absolutePath
                        programParameters = runClient.calculateArgs(project).map { '"' + it + '"' }.joinToString(" ")
                        jvmArgs = runClient.calculateJvmArgs(project).map { '"' + it + '"' }.joinToString(" ") +
                          ' ' + runClient.systemProperties.map { "\"-D" + it.key + '=' + it.value.toString() + '"' }
                          .joinToString(" ")
                      }
                    })
                    self.add(Application("Run Server (IJ Native, Deprecated)", project).apply {
                      mainClass = "GradleStartServer"
                      moduleName = project.name + ".ideVirtualMain"
                      afterEvaluate {
                        val runServer = tasks.runServer.get()
                        workingDirectory = runServer.workingDir.absolutePath
                        programParameters = runServer.calculateArgs(project).map { '"' + it + '"' }.joinToString(" ")
                        jvmArgs = runServer.calculateJvmArgs(project).map { '"' + it + '"' }.joinToString(" ") +
                          ' ' + runServer.systemProperties.map { "\"-D" + it.key + '=' + it.value.toString() + '"' }
                          .joinToString(" ")
                      }
                    })
                    */
                }
                "compiler" {
                    val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
                    afterEvaluate {
                        self.javac.moduleJavacAdditionalOptions = mapOf(
                            (project.name + ".main") to
                                    tasks.compileJava.get().options.compilerArgs.map { '"' + it + '"' }.joinToString(" ")
                        )
                    }
                }
            }
        }
    }
}

//tasks.processIdeaSettings.configure {
//    dependsOn(tasks.injectTags)
//}