package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PrankRepository
import com.example.model.PrankItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NotifyPrank", appName)
  }

  @Test
  fun `add and retrieve prank in repository`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = PrankRepository(context)

    val item = PrankItem(
      id = "test-123",
      senderName = "Bank Alert",
      appType = "bank",
      appLabel = "City Bank",
      title = "Deposited",
      message = "Tk 50,000 received",
      scheduledTimeMillis = System.currentTimeMillis() + 5000L
    )

    repo.addPrank(item)
    val pranks = repo.pranks.value
    assertNotNull(pranks.find { it.id == "test-123" })
    assertEquals("Bank Alert", pranks.find { it.id == "test-123" }?.senderName)
  }
}
