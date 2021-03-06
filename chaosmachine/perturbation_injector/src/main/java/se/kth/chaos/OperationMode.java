package se.kth.chaos;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

public enum OperationMode {
    NONE {
        public InsnList generateByteCode(TryCatchBlockNode tryCatchBlock, MethodNode methodNode, ClassNode classNode, int tcIndex, AgentArguments arguments) {
            // we use this mode together with outputting class files to compare the overhead of file size
            return null;
        }
    },
	SCIRCUIT {
        public InsnList generateByteCode(TryCatchBlockNode tryCatchBlock, MethodNode methodNode, ClassNode classNode, int tcIndex, AgentArguments arguments) {
			InsnList list = new InsnList();

            String tcIndexInfo = String.format("%s@%s(%s),%s,%s", tcIndex, tryCatchBlock.start.getLabel().toString(), tryCatchBlock.type, methodNode.name, classNode.name);
            list.add(new LdcInsnNode(tcIndexInfo));
            list.add(new LdcInsnNode(tryCatchBlock.type));
            list.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "se/kth/chaos/ChaosMonkey",
                    "throwException",
                    "(Ljava/lang/String;Ljava/lang/String;)V",
                    false // this is not a method on an interface
            ));
            
            return list;
        }
	},
    ANALYZETC {
        public InsnList generateByteCode(TryCatchBlockNode tryCatchBlock, MethodNode methodNode, ClassNode classNode, int tcIndex, AgentArguments arguments) {
            InsnList list = new InsnList();

            String tcIndexInfo = String.format("%s@%s(%s),%s,%s", tcIndex, tryCatchBlock.start.getLabel().toString(), tryCatchBlock.type, methodNode.name, classNode.name);
            list.add(new LdcInsnNode(tcIndexInfo));
            list.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "se/kth/chaos/LogTryCatchInfo",
                    "printInfo",
                    "(Ljava/lang/String;)V",
                    false // this is not a method on an interface
            ));

            ChaosMonkey.registerTrycatchInfo(arguments, tcIndexInfo, arguments.defaultMode());

            return list;
        }
    },
    MEMCACHED {
        public InsnList generateByteCode(TryCatchBlockNode tryCatchBlock, MethodNode methodNode, ClassNode classNode, int tcIndex, AgentArguments arguments) {
            InsnList list = new InsnList();

            String tcIndexInfo = String.format("%s@%s(%s),%s,%s", tcIndex, tryCatchBlock.start.getLabel().toString(), tryCatchBlock.type, methodNode.name, classNode.name);
            list.add(new LdcInsnNode(tcIndexInfo));
            list.add(new LdcInsnNode(tryCatchBlock.type));
            list.add(new LdcInsnNode(arguments.defaultMode()));
            list.add(new LdcInsnNode(arguments.memcachedHost()));
            list.add(new IntInsnNode(Opcodes.SIPUSH ,arguments.memcachedPort()));
            list.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "se/kth/chaos/ChaosMonkey",
                    "doChaos",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V",
                    false // this is not a method on an interface
            ));

//            Runnable registerTask = () -> {ChaosMonkey.registerTrycatchInfo(arguments, tcIndexInfo, "off");};
//            Thread registerThread = new Thread(registerTask);
//            registerThread.start();
            ChaosMonkey.registerTrycatchInfo(arguments, tcIndexInfo, arguments.defaultMode());

            return list;
        }
    };

    public static OperationMode fromLowerCase(String mode) {
        return OperationMode.valueOf(mode.toUpperCase());
    }

    public abstract InsnList generateByteCode(TryCatchBlockNode tryCatchBlock, MethodNode methodNode, ClassNode classNode, int tcIndex, AgentArguments arguments);
}
