package example.examplemod.mineui.utils

import example.examplemod.mineui.style.ImageFit
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

fun DrawStack.drawImage(fit: ImageFit, size: Size, src: ResourceLocation) {
    Minecraft.getInstance().textureManager.bindForSetup(src)

    val originalW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
    val originalH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)

    val scaleX = size.width.toDouble() / originalW
    val scaleY = size.height.toDouble() / originalH

    when (fit) {
        ImageFit.Stretch -> {
            val scaledW = originalW * scaleX
            val scaledH = originalH * scaleY

            drawImage(0, 0, scaledW.toInt(), scaledH.toInt(), src)
        }
        ImageFit.Contain -> {
            val maxScale = max(scaleX, scaleY)
            val scaledW = originalW * maxScale
            val scaledH = originalH * maxScale

            drawImage(0, 0, scaledW.toInt(), scaledH.toInt(), src)
        }
        ImageFit.Cover -> {
            val minScale = min(scaleX, scaleY)
            val scaledW = originalW * minScale
            val scaledH = originalH * minScale

            drawImage(0, 0, 0f, 0f, size.width, size.height, scaledW.toInt(), scaledH.toInt(), src)
        }
    }
}
/*
Will be released in Future
S
fun shader(type: Int, source: String): Int {
    val shader = GL20C.glCreateShader(type)
    GL20C.glShaderSource(shader, """
        uniform sampler2D image;

        out vec4 FragmentColor;

        uniform float offset[3] = float[](0.0, 1.3846153846, 3.2307692308);
        uniform float weight[3] = float[](0.2270270270, 0.3162162162, 0.0702702703);

        void main(void) {
            FragmentColor = texture2D(image, vec2(gl_FragCoord) / 1024.0) * weight[0];
            for (int i=1; i<3; i++) {
                FragmentColor += texture2D(image, (vec2(gl_FragCoord) + vec2(0.0, offset[i])) / 1024.0) * weight[i];
                FragmentColor += texture2D(image, (vec2(gl_FragCoord) - vec2(0.0, offset[i])) / 1024.0) * weight[i];
            }
        }
    """.trimIndent())
    GL20C.glCompileShader(shader)
    return shader
}

fun program(vararg shaders: Int): Int {
    val program = GL20C.glCreateProgram()

    for (shader in shaders) {
        GL20C.glAttachShader(program, shader)
    }

    GL20C.glLinkProgram(program)

    for (s in shaders) {
        GL20C.glDetachShader(program, s)
    }
    return program
}

fun PoseStack.blur() {
    GlStateManager._glUseProgram()
}
 */