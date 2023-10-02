package works.hop.first

import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class Dsl {

    static void pipeline(@DelegatesTo(value = PipelineDSL, strategy = Closure.DELEGATE_ONLY) final Closure closure) {
        final PipelineDSL dsl = new PipelineDSL()
        closure.delegate = dsl
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.call()
    }
}

class PipelineDSL {

    final Placeholder any = Placeholder.any
    static final ConcurrentMap<String, String> env = [:] as ConcurrentHashMap

    static void agent(final Placeholder any) {
        println "Running pipeline using 'any' available agent"
    }

    static void environment(@DelegatesTo(value = Map, strategy = Closure.DELEGATE_FIRST) final Closure closure) {
        env.with(closure)
        println env
    }

    static void stages(@DelegatesTo(value = StagesDSL, strategy = Closure.DELEGATE_ONLY) final Closure closure) {
        final StagesDSL dsl = new StagesDSL()
        closure.delegate = dsl
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.call()

        dsl.stages.each { stage ->
            stage.run()
        }
    }

    enum Placeholder {
        any
    }
}

class StagesDSL {

    protected final List<Stage> stages = []

    void stage(final String name, @DelegatesTo(value = StageDSL, strategy = Closure.DELEGATE_ONLY) final Closure closure) {
        stages << new Stage(name, closure)
    }
}

class StageDSL {

    static void steps(@DelegatesTo(value = StepsDSL, strategy = Closure.DELEGATE_ONLY)
                      @ClosureParams(value = SimpleType, options = ["java.util.Map"]) final Closure closure) {
        final StepsDSL dsl = new StepsDSL()
        closure.delegate = dsl
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.call(PipelineDSL.env)
    }
}

class Stage {

    final String name
    final Closure closure

    Stage(String name, Closure closure) {
        this.name = name
        this.closure = closure
    }

    void run() {
        println "==> Running ${name} stage"

        final StageDSL dsl = new StageDSL()
        closure.delegate = dsl
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.call()
    }
}

class StepsDSL {

    static void sh(final String script) {
        sh(script: script, returnsStdout: false)
    }

    static Object sh(@NamedParams([
            @NamedParam(value = "script", type = String, required = true),
            @NamedParam(value = "returnsStdout", type = Boolean)]) final Map params) {

        final Process p = Runtime.getRuntime().exec(params.script.toString())
        p.waitFor()

        println "+ ${params.script}"

        if (p.exitValue() == 0) {
            if (params.returnStdout) {
                return p.text
            }

            println p.text
        } else {
            println p.err.text
        }
    }

    static void echo(final String message) {
        println "[ECHO] ${message}"
    }
}